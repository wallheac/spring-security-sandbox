package com.mentalhealth.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/welcome").setViewName("welcome-user");
    }
}
