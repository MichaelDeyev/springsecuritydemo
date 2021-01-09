package com.deyev.springsecuritydemo.config;

import com.deyev.springsecuritydemo.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // необходимый метод для конфигурации собственной "системы безопасности"
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//Cross Site Request Forgery. (способ защиты от csrf угрозы)
                .authorizeRequests()// какой изер на какие урлы URL будет иметь доступ.
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()// every request have to be authenticated with Basic64 technology
                .loginPage("/auth/login").permitAll()// define login Page
                .defaultSuccessUrl("/auth/success")// if login successful
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))// use POST method instead of GET method wile logout
                .invalidateHttpSession(true)//invalidation session
                .clearAuthentication(true)//clear all Authentication tips
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/auth/login");
    }

    // admin creating with encoding password
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities(Role.ADMIN.getAuthorities())
                        .build(),
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user"))
                        .authorities(Role.USER.getAuthorities())
                        .build()
        );
    }

    //encoder with strength 12
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
