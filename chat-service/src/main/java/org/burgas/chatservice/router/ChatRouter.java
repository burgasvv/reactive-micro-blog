package org.burgas.chatservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.ChatResponse;
import org.burgas.chatservice.handler.ChatHandler;
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
public class ChatRouter {

    private final ChatHandler chatHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/chats/by-identity/{identity-id}", method = RequestMethod.GET,
                            beanClass = ChatHandler.class, beanMethod = "handleFindChatsByIdentityId",
                            operation = @Operation(
                                    operationId = "handleFindChatsByIdentityId",
                                    summary = "Получение чатов пользователя",
                                    parameters = {
                                            @Parameter(
                                                    name = "identity-id", in = ParameterIn.PATH,
                                                    description = "Identity id path variable"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = ChatResponse.class)
                                                    )
                                            ),
                                            @ApiResponse(
                                                    responseCode = "500", description = "Server response error",
                                                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                                            )
                                    }
                            )
                    ),

                    @RouterOperation(
                            path = "/chats/{chat-id}", method = RequestMethod.GET,
                            beanClass = ChatHandler.class, beanMethod = "handleFindById",
                            operation = @Operation(
                                    operationId = "handleFindById",
                                    summary = "Получение чата по идентификатору",
                                    parameters = {
                                            @Parameter(
                                                    name = "chat-id", in = ParameterIn.PATH,
                                                    description = "Chat id path variable"
                                            ),
                                            @Parameter(
                                                    name = "identityId", in = ParameterIn.DEFAULT,
                                                    description = "Identity id query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = ChatResponse.class)
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
    public RouterFunction<ServerResponse> chats() {
        return RouterFunctions.route()
                .GET("/chats/by-identity/{identity-id}", chatHandler::handleFindChatsByIdentityId)
                .GET("/chats/{chat-id}", chatHandler::handleFindById)
                .build();
    }
}
