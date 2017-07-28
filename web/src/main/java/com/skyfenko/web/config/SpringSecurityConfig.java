package com.skyfenko.web.config;

import com.skyfenko.web.auth.handler.KalahAuthenticationSuccessHandler;
import com.skyfenko.web.constants.URIConstants;
import com.skyfenko.web.error.KalahAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Spring Security config.
 * Defines which URIs are permitted and who can access them
 * Defines 2 usernames and passwords for them. Please check the description to this task to find them out.
 * Defines which resources could be populated without authentication
 *
 * @author Stanislav Kyfenko
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/403").permitAll()
                .antMatchers(URIConstants.Api.TURNS, URIConstants.Api.GAME_INFO, URIConstants.Api.RESTART_GAME, "/game", "/logout")
                .hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().successHandler(authenticationSuccessHandler()).defaultSuccessUrl("/game")
                .and()
                .logout().logoutUrl("/logout").permitAll().logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.sessionManagement().maximumSessions(1);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new KalahAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new KalahAccessDeniedHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("south")
                .password("$2a$10$4oEx/i2sQsH9xBtdAFxmxeOoiGq7r2MZDYdWIsoc.7DRcNUE8unTG").roles("USER")
                .and()
                .withUser("north")
                .password("$2a$10$BHTL8tzlR3h90ktqaXiO3OnvoZ.ktOc6AHkC0FFrgNB6ahXaWKFLK").roles("USER")
                .and()
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**", "/css/**", "/js/**",
                        "/images/**", "/webjars/**");
    }
}
