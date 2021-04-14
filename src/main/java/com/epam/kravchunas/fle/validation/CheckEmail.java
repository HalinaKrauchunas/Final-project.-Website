package com.epam.kravchunas.fle.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckEmailValidator.class)
public @interface CheckEmail {
    String message() default "Email должен быть корректным адресом электронной почты";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
