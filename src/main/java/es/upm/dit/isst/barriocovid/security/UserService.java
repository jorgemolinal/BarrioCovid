package es.upm.dit.isst.barriocovid.security;

import es.upm.dit.isst.barriocovid.dto.UserDto;
import es.upm.dit.isst.barriocovid.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Usuario save(UserDto registrationDto);
}