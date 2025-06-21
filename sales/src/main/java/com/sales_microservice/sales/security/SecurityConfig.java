package com.sales_microservice.sales.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Configuración de permisos por roles
                    http.requestMatchers(HttpMethod.POST, "/sales/create").hasAnyRole("ADMIN", "CLIENT");
                    http.requestMatchers(HttpMethod.GET, "/sales/get").hasAnyRole("ADMIN", "CLIENT", "DELIVERER");
                    http.requestMatchers(HttpMethod.GET, "/sales/get/{id}").hasAnyRole("ADMIN", "CLIENT", "DELIVERER");
                    http.requestMatchers(HttpMethod.GET, "/sales/email/{email}").hasAnyRole("ADMIN", "CLIENT");
                    http.requestMatchers(HttpMethod.PATCH, "/sales/update/{id}").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/sales/delete/{id}").hasRole("ADMIN");

                    http.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("admin")
                .password("123")
                .roles("ADMIN")
                .build());

        userDetailsList.add(User.withUsername("client")
                .password("123")
                .roles("CLIENT")
                .build());

        userDetailsList.add(User.withUsername("deliverer")
                .password("123")
                .roles("DELIVERER")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}