package com.epam.kravchunas.fle.validation;

import com.epam.kravchunas.fle.entities.user.User;
import com.epam.kravchunas.fle.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsernameUniquenessValidator implements Validator {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.isPresentUser(user.getUsername())){
            errors.rejectValue("username", "", "Пользователь с таким именем уже зарегистрирован");
        }
    }
}
