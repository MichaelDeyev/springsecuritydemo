package com.deyev.springsecuritydemo.config;

import com.deyev.springsecuritydemo.model.Permission;
import com.deyev.springsecuritydemo.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // необходимый метод для конфигурации собственной "системы безопасности"
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//Cross Site Request Forgery. (способ защиты от csrf угрозы)
                .authorizeRequests()// какой изер на какие урлы URL будет иметь доступ.
                .antMatchers("/").permitAll()//к урле "/" имеют доступ все пользователи.
                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(Permission.DEVELOPERS_READ.getPermission())// на чтение (get-request) имеют доступ только пользователи с правом чтения
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())// на запись (post-request) имеют доступ только пользователи с правом записи
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())// на удаление (delete-request) имеют доступ только пользователи с правом записи
                .anyRequest().authenticated().and().httpBasic()// every request have to be authenticated with Basic64 technology
        ;
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
