package com.gui.payment_sys.config;

import com.gui.payment_sys.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers("/hello").permitAll();
                    auth.requestMatchers("/debug").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                            Set<GrantedAuthority> authorities = new HashSet<>(oauthUser.getAuthorities());
                            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                            AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                    oauthUser, null, authorities
                            );
                            userService.processOAuthPostLogin(oauthUser.getAttribute("email"),oauthUser.getAttribute("name"));
                            SecurityContextHolder.getContext().setAuthentication(auth);
                            response.sendRedirect("/hello");
                        })
                )
                .build();
    }
}
