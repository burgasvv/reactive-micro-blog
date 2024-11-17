package org.burgas.identityservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.handler.IdentityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class IdentityRouter {

    private final IdentityHandler identityHandler;

    @Bean
    public RouterFunction<ServerResponse> identityRouterFunction() {
        return RouterFunctions.route()
                .GET("/identities", identityHandler::handleFindAll)
                .GET("/identities/by-id/{identity-id}", identityHandler::handleFindById)
                .GET("/identities/{username}", identityHandler::handleFindByUsername)
                .POST("/identities/create", identityHandler::handleCreateIdentity)
                .PUT("/identities/update", identityHandler::handleUpdateIdentity)
                .DELETE("/identities/delete/{identity-id}", identityHandler::handleDeleteIdentity)
                .build();
    }
}
