package org.burgas.gatewayserver.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.gatewayserver.dto.IdentityPrincipal;
import org.burgas.gatewayserver.handler.AuthenticationHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationRouter {

    private final AuthenticationHandler authenticationHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/authentication/principal", method = RequestMethod.GET,
                            beanClass = AuthenticationHandler.class, beanMethod = "getPrincipal",
                            operation = @Operation(
                                    operationId = "getPrincipal", summary = "Получить авторизованного пользователя",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityPrincipal.class)
                                                    )
                                            ),
                                            @ApiResponse(
                                                    responseCode = "500", description = "Server response error",
                                                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                                            )
                                    }
                            )
                    )
            }
    )
    public RouterFunction<ServerResponse> authentication() {
        return RouterFunctions.route()
                .GET("/authentication/principal", authenticationHandler::getPrincipal)
                .build();
    }
}
