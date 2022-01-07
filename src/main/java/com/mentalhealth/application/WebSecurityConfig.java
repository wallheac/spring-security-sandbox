package com.mentalhealth.application;

import com.mentalhealth.application.authentication.MentalHealthAuthenticationSuccessHandler;
import com.mentalhealth.application.authentication.MentalHealthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MentalHealthUserDetailsService mentalHealthUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/home", "/registration")
                .permitAll()
                .antMatchers("/provider/**").hasAnyAuthority("PROVIDER")
                .antMatchers("/consumer/**").hasAnyAuthority("CONSUMER")
                .and()
                .formLogin()
              .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/perform_login")
                .successHandler(mentalHealthSuccessHandler())
                .failureUrl("/login.html?error=true")
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID");
    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mentalHealthUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler mentalHealthSuccessHandler(){
        return new MentalHealthAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
