package com.mentalhealth.application.controllers;

import com.mentalhealth.application.authentication.User;
import com.mentalhealth.application.authentication.UserDto;
import com.mentalhealth.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid UserDto userDto,
                                     HttpServletRequest request,
                                     Errors errors) {

            userService.registerNewUserAccount(userDto);
            return "/login";
    }
}
