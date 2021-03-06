package com.itguang.springsecurityjwt.config;

import com.itguang.springsecurityjwt.filter.JWTAuthenticationFilter;
import com.itguang.springsecurityjwt.filter.JWTLoginFilter;
import com.itguang.springsecurityjwt.service.impl.CustomAuthenticationProvider;
import com.itguang.springsecurityjwt.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author itguang
 * @create 2018-01-02 10:32
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

       // auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        // 使用自定义身份验证组件
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService,bCryptPasswordEncoder));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用 csrf
        http.cors().and().csrf().disable().authorizeRequests()
                //允许以下请求
                .antMatchers("/hello").permitAll()
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                //验证登陆
                .addFilter(new JWTLoginFilter(authenticationManager()))
                //验证token
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }



}
