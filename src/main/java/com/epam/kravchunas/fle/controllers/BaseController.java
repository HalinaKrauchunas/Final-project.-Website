package com.epam.kravchunas.fle.controllers;

import com.epam.kravchunas.fle.entities.user.User;
import com.epam.kravchunas.fle.services.user.MailSenderService;
import com.epam.kravchunas.fle.services.user.RoleService;
import com.epam.kravchunas.fle.services.user.UserService;
import com.epam.kravchunas.fle.validation.EmailUniquenessValidator;
import com.epam.kravchunas.fle.validation.UsernameUniquenessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class BaseController {

    private UserService userService;
    private EmailUniquenessValidator emailUniquenessValidator;
    private UsernameUniquenessValidator usernameUniquenessValidator;
    private RoleService roleService;
    private MailSenderService mailSenderService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setEmailUniquenessValidator(EmailUniquenessValidator emailUniquenessValidator) {
        this.emailUniquenessValidator = emailUniquenessValidator;
    }

    @Autowired
    public void setUsernameUniquenessValidator(UsernameUniquenessValidator usernameUniquenessValidator) {
        this.usernameUniquenessValidator = usernameUniquenessValidator;
    }

    @Autowired
    public void setMailSenderService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "publicPages/home";
    }

    @GetMapping("/login")
    public String login() {
        return "publicPages/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "publicPages/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "publicPages/registration";
    }

    @PostMapping("/saveUser")
    public String tryingToSaveUser(@Valid @ModelAttribute("user") User usr,
                                   BindingResult bindingResult, Model model) {
        usernameUniquenessValidator.validate(usr, bindingResult);
        emailUniquenessValidator.validate(usr, bindingResult);
        if (bindingResult.hasErrors()) {
            return "publicPages/registration";
        } else {
            usr.setPassword(passwordEncoder.encode(usr.getPassword()));
            usr.getRoles().add(roleService.findByName("ROLE_USER"));
            usr.setActivationCode(UUID.randomUUID().toString());
            userService.saveUser(usr);
            String message = String.format(
                    "Здравствуйте, %s! \n" +
                            "Для подтверждения почтового ящика перейдите, пожалуйста, "
                            + "по ссылке: http://localhost:2021/activate/%s",
                    usr.getUsername(),
                    usr.getActivationCode()
            );
            mailSenderService.send(usr.getEmail(), "Activation code", message);
            model.addAttribute("message", "На почту пришло сообщение со ссылкой для активации аккаунта");
            return "publicPages/home";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", "Активация прошла успешно");
        } else {
            model.addAttribute("message", "Ссылка недействительна");
        }
        return "publicPages/login";
    }

    @GetMapping("/main")
    public String main() {
        return "afterAuthenticationPage/main";
    }

    @PostMapping("/main")
    public String successLogin(){
        return "afterAuthenticationPage/main";
    }
}



