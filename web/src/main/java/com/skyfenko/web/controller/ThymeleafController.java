package com.skyfenko.web.controller;

import com.skyfenko.domain.exceptions.impl.UnableToFetchUserException;
import com.skyfenko.web.constants.URIConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Mostly control model-and-view aspect of web user flows: returning views with model and redirecting
 *
 * @author Stanislav Kyfenko
 */
@Controller
public class ThymeleafController {

    /**
     * Go to /game page
     */
    @GetMapping("/")
    public ModelAndView home() {
        return gamePage();
    }

    /**
     * Go to /game page if user is authenticated, otherwise, go to /login
     */
    @GetMapping(URIConstants.Flow.GAME)
    public ModelAndView gamePage() {
        ModelAndView modelAndView = new ModelAndView();
        String attributeValue;
        try {
            attributeValue = currentUsername();
        } catch (UnableToFetchUserException e) {
            // redirect to login page if you is undefined
            return new ModelAndView("login");
        }
        modelAndView.addObject("username", attributeValue);
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @GetMapping(URIConstants.Flow.LOGIN)
    public String login() {
        return "login";
    }

    @GetMapping(URIConstants.Flow.ACCESS_DENIED)
    public String error403() {
        return "error/403";
    }

    /**
     * Find authenticated user. If it doesn't exists, throw exception
     *
     * @return username of authenticated user
     */
    private String currentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (User.class.isAssignableFrom(principal.getClass())) {
            User user = (User) principal;
            return user.getUsername();
        }
        throw new UnableToFetchUserException("cannot fetch user from security context. check whether user is authenticated.");
    }

}
