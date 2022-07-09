package com.example.demo.utils.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
public class LoginPointCut {
    private final HttpServletRequest request;

    public LoginPointCut(HttpServletRequest request) {
        this.request = request;
    }

    @Around(value = "@within(Monitor) || @annotation(Monitor)")
    public String before(ProceedingJoinPoint joinPoint) throws Throwable {
        if (request.getSession().getAttribute("username") == null){
            return "redirect:/login";
        }
        joinPoint.proceed();
        return "/main/Room_Create";
    }
}
