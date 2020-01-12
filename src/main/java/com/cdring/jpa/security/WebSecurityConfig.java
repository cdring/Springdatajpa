package com.cdring.jpa.security;

import com.cdring.jpa.service.UserDetailsServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsServiceimpl userDetailsService;

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new UserDetailsServiceimpl();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() {
        return new AuthenticationTokenFilter();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.sessionManagement().disable()
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/login",
                        "/register").permitAll()
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
