package org.burgas.gatewayserver.router;

import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.handler.AuthenticationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
@RequiredArgsConstructor
public class AuthenticationRouter {

    private final AuthenticationHandler authenticationHandler;

    @Bean
    public RouterFunction<ServerResponse> authentication() {
        return RouterFunctions.route(GET("/authentication/principal"), authenticationHandler::getPrincipal);
    }
}
