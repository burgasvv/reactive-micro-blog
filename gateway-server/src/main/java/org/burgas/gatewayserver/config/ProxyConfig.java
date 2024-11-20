package org.burgas.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "identities",
                        predicateSpec -> predicateSpec
                                .path("/identities/**", "/friendship/**")
                                .uri("lb://identity-service")
                )
                .route(
                        "posts",
                        predicateSpec -> predicateSpec
                                .path("/comments/**", "/posts/**")
                                .uri("lb://post-service")
                )
                .route(
                        "chats",
                        predicateSpec -> predicateSpec
                                .path("/chats/**", "/messages/**")
                                .uri("lb://chat-service")
                )
                .build();
    }
}
