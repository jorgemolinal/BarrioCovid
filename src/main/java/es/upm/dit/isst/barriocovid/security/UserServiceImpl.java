package es.upm.dit.isst.barriocovid.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import es.upm.dit.isst.barriocovid.dto.UserDto;
import es.upm.dit.isst.barriocovid.model.Role;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.RoleRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    private UsuarioRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UsuarioRepository userRepository,RoleRepository roleRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository= roleRepository;
    }

    @Override
    public Usuario save(UserDto userDto) {
        Usuario saveUser = new Usuario(userDto.getUsername(),  passwordEncoder.encode(userDto.getPassword()),userDto.getNombre(),userDto.getEmail(),userDto.getDireccion(),userDto.isVulnerable(),userDto.getTipo());
        Optional<Role> role= roleRepository.findByName(userDto.getTipo());
        if(role.isEmpty()) {
            role= Optional.of(roleRepository.save(new Role(userDto.getTipo())));
        }
        saveUser.setRoles(Arrays.asList(role.get()));

        return userRepository.save(saveUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Nombre de usuario o contraseña incorrectos");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), mapRolesToAuthorities(user.get().getRoles()));
    }

    private Collection <?extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}