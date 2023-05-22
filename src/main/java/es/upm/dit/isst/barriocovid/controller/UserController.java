package es.upm.dit.isst.barriocovid.controller;


import es.upm.dit.isst.barriocovid.dto.UserDto;
import es.upm.dit.isst.barriocovid.model.Role;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.RoleRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;
import es.upm.dit.isst.barriocovid.security.UserService;
import org.apache.catalina.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Principal principal) {
        String userName= principal.getName();

        Optional<Usuario> usuario= usuarioRepository.findByEmail(userName);

        if(usuario.get().getRoles().get(0).getName().toString().equals("COMPRADOR")) {
            return "redirect:/comprador/" + usuario.get().getId();
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VENDEDOR")) {
            return "redirect:/misproductos";
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VOLUNTARIO")) {
            return "redirect:/voluntario/" + usuario.get().getId();
        }

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        String userName= principal.getName();

        Optional<Usuario> usuario= usuarioRepository.findByEmail(userName);

        if(usuario.get().getRoles().get(0).getName().toString().equals("COMPRADOR")) {
            return "redirect:/comprador/" + usuario.get().getId();
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VENDEDOR")) {
            return "redirect:/misproductos";
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VOLUNTARIO")) {
            return "redirect:/voluntario/" + usuario.get().getId();
        }

        return "redirect:/login";
    }

    @GetMapping("/login.html")
    public String home1(Principal principal) {
        String userName= principal.getName();

        Optional<Usuario> usuario= usuarioRepository.findByEmail(userName);

        if(usuario.get().getRoles().get(0).getName().toString().equals("COMPRADOR")) {
            return "redirect:/comprador/" + usuario.get().getId();
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VENDEDOR")) {
            return "redirect:/misproductos";
        }
        else if(usuario.get().getRoles().get(0).getName().toString().equals("VOLUNTARIO")) {
            return "redirect:/voluntario/" + usuario.get().getId();
        }

        return "redirect:/login";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserDto registrationDto, RedirectAttributes model) {
        try{
            userService.save(registrationDto);
            model.addFlashAttribute("alert","alert alert-success");
            model.addFlashAttribute("message","Usuario registrado correctamente.");
        }
        catch(Exception e){
            model.addFlashAttribute("alert","alert alert-danger");
            model.addFlashAttribute("message", "Ya existe una cuneta registrada con este correo o usuario.");
        }

        return "redirect:/registration";
    }

}
