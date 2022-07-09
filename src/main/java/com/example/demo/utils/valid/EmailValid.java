package com.example.demo.utils.valid;

import com.google.api.client.json.webtoken.JsonWebToken;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface EmailValid {
    String message() default "Invalid email";
    Class<?>[] groups() default {};
    Class<? extends JsonWebToken.Payload>[] payload() default {};
}
