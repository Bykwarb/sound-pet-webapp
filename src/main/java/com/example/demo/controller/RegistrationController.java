package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationTokenEntity;
import com.example.demo.events.OnRegistrationCompleteEvent;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Calendar;
import java.util.Date;

@Controller("/")
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserEntityService userService;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public RegistrationController(UserEntityService userService, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    @RequestMapping("/registration")
    public String showRegistrationPage(WebRequest request, Model model){
        logger.info("1st");
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        return "/sign/sign-up";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserEntity userEntity, HttpServletRequest request, Errors errors){
        if (userEntity.getPassword().toCharArray().length <= 8 && userEntity.getPassword() != userEntity.getRepassword()){
            ModelAndView mav = new ModelAndView("/sign/sign-up", "userTransfer", userEntity);
            mav.addObject("message", "Password must contain 8 or more characters.");
            return mav;
        }
        logger.info(userEntity.getPassword() + " " + userEntity.getRepassword());
        logger.info(String.valueOf(userEntity.getPassword().equals(userEntity.getRepassword())));
        userEntity.setRegistrationDate(new Date());
        try {
            UserEntity registered = userService.registerNewUserAccount(userEntity);
            String appUrl = request.getContextPath();

            //After registration triggers RegistrationListener which generates a token and sends a message to the mail.
            // The user has 2 status options - enabled and not enabled. If user not enabled - program won't let him in.
            //To enable the user, you need to follow the link in the letter on the specified mail.
            // If the link exists for more than 24 hours, then it becomes invalid.

            publisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        }catch (UserAlreadyExistsException e){
            ModelAndView mav = new ModelAndView("/sign/sign-up", "userTransfer", userEntity);
            mav.addObject("message", "An account for that email already exists.");
            return mav;
        }
        return new ModelAndView("/sign/success", "user", userEntity);
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){
        VerificationTokenEntity verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null){
            String message = "InvalidToken";
            model.addAttribute("message", message);
            return "redirect:/badUser";
        }
        UserEntity user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            String messageValue = "Token expired";
            model.addAttribute("message", messageValue);
            return "redirect:/sign/badUser";
        }
        user.setEnabled(true);
        userService.registerAfterConfirmation(user);
        return "redirect:/login";
    }
}
