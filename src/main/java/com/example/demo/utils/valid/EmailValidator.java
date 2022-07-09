package com.example.demo.utils.valid;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {
    private Pattern pattern;
    private Matcher matcher;
    //private static final String EMAIL_PATTERN =
    //        "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" +
    //                "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    private static final String EMAIL_PATTERN = "";
    @Override
    public void initialize(EmailValid constraintAnnotation) {
    }
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return validateEmail(s);
    }
    private boolean validateEmail(String email){
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
