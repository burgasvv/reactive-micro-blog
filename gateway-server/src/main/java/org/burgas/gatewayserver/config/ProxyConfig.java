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
                                .path(
                                        "/identities/**", "/friendship/**",
                                        "/identity-service/v3/api-docs", "/identity-service/v3/api-docs/**"
                                )
                                .uri("http://localhost:8888")
                )
                .route(
                        "posts",
                        predicateSpec -> predicateSpec
                                .path(
                                        "/comments/**", "/posts/**",
                                        "/post-service/v3/api-docs", "/post-service/v3/api-docs/**"
                                )
                                .uri("http://localhost:9000")
                )
                .route(
                        "chats",
                        predicateSpec -> predicateSpec
                                .path(
                                        "/chats/**", "/messages/**",
                                        "/chat-service/v3/api-docs", "/chat-service/v3/api-docs/**"
                                )
                                .uri("http://localhost:9010")
                )
                .route(
                        "communities",
                        predicateSpec -> predicateSpec
                                .path(
                                        "/communities/**",
                                        "/community-service/v3/api-docs", "/community-service/v3/api-docs/**"
                                )
                                .uri("http://localhost:9020")
                )
                .build();
    }
}
