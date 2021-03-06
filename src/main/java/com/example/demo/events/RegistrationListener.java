package com.example.demo.events;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);
    private final UserEntityService service;
    private final JavaMailSender javaMailSender;

    public RegistrationListener(UserEntityService service, JavaMailSender javaMailSender) {
        this.service = service;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
    //After registration triggers OnRegistrationCompleteEvent which generates a token and sends a message to the mail.
    // The user has 2 status options - enabled and not enabled. If user not enabled - program won't let him in.
    //To enable the user, you need to follow the link in the letter on the specified mail.
    // If the link exists for more than 24 hours, then it becomes invalid.
    private void confirmRegistration(OnRegistrationCompleteEvent event){
        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Follow the link to confirm your registration";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        javaMailSender.send(email);
        logger.info("Mail has been sent");
    }
}
