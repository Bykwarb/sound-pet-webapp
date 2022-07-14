package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserEntityService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserEntityService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");
        logger.info(username);
        if (username == null || username.isEmpty()) {
            logger.info("Redirect to login page");
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        return "redirect:/room";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
        logger.info("User see login page");
        return "/sign/sign-in";
    }
    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request){
        logger.info("User logout");
        request.getSession(true).invalidate();
        return "redirect:/login";
    }

    @PostMapping("/login")
    public ModelAndView doLogin(HttpServletRequest request,
                                @RequestParam String email,
                                @RequestParam String password) {
        ModelAndView modelAndView;
        UserEntity user = userService.loginUser(email);
            if (user == null) {
                modelAndView = new ModelAndView("/sign/sign-in", "message", new String("User not found!"));
                logger.info("User not found!");
                return modelAndView;
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                modelAndView = new ModelAndView("/sign/sign-in", "message", new String("Error password!"));
                logger.info("Error password!");
                return modelAndView;
            } else if (!user.isEnabled()) {
                modelAndView = new ModelAndView("/sign/sign-in", "message", new String("Email hasn't confirmed!"));
                logger.info("Email hasn't confirmed");
                return modelAndView;
            }
            request.getSession().setAttribute("username", user.getUsername());
            request.getSession().setAttribute("email", user.getEmail());
            modelAndView = new ModelAndView("redirect:/");
            return modelAndView;
    }
}
