package org.burgas.gatewayserver.config;

import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.service.CustomReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomReactiveUserDetailsService userDetailsService;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic(
                        httpBasicSpec -> httpBasicSpec
                                .authenticationManager(reactiveAuthenticationManager())
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec

                                .pathMatchers(
                                        "/v3/api-docs", "/v3/api-docs/**",
                                        "/identity-service/v3/api-docs","/identity-service/v3/api-docs/**",
                                        "/post-service/v3/api-docs","/post-service/v3/api-docs/**",
                                        "/chat-service/v3/api-docs","/chat-service/v3/api-docs/**",
                                        "/community-service/v3/api-docs","/community-service/v3/api-docs/**",

                                        "/swagger-ui.html", "/webjars/swagger-ui/**",
                                        "/identity-service/swagger-ui.html", "/identity-service/webjars/swagger-ui/**",
                                        "/post-service/swagger-ui.html", "/post-service/webjars/swagger-ui/**",
                                        "/chat-service/swagger-ui.html", "/chat-service/webjars/swagger-ui/**",
                                        "/community-service/swagger-ui.html","/community-service/webjars/swagger-ui/**"
                                )
                                .permitAll()

                                .pathMatchers(
                                        "/identities/create", "/authentication/principal"
                                )
                                .permitAll()

                                .pathMatchers(
                                        "/identities/**", "/posts/**", "/comments/**",
                                        "/chats/**", "/messages/**", "/friendship/**", "/communities/**"
                                )
                                .hasAnyAuthority("ADMIN", "USER")
                )
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
