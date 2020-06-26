package com.inventory.config;

import com.inventory.security.InventoryAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class InventorySecurityConfiguration extends WebSecurityConfigurerAdapter {

    //@Autowired
    private AuthenticationEntryPoint authenticationEntryPoint = new InventoryAuthenticationEntryPoint();

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                @Value("${spring.security.user.name}") String username,
                                @Value("${spring.security.user.password}") String password) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(username).password(password)
                .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}