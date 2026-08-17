package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.exceptions.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public org.springframework.web.servlet.ModelAndView handleGeneralException(Exception ex) {
        org.springframework.web.servlet.ModelAndView mav = new org.springframework.web.servlet.ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", "Request URL");
        mav.setViewName("error/500");
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public org.springframework.web.servlet.ModelAndView handleResourceNotFound(ResourceNotFoundException ex) {
        org.springframework.web.servlet.ModelAndView mav = new org.springframework.web.servlet.ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", "Request URL");
        mav.setViewName("error/404");
        return mav;
    }

}
