package org.burgas.chatservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.chatservice.dto.MessageRequest;
import org.burgas.chatservice.handler.MessageHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class MessageRouter {

    private final MessageHandler messageHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/messages/events", method = RequestMethod.GET,
                            beanClass = MessageHandler.class, beanMethod = "handleServerSendMessageEvent",
                            operation = @Operation(
                                    operationId = "handleServerSendMessageEvent",
                                    summary = "Получение тестового оповещения от сервера",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                                                            schema = @Schema(implementation = ServerSentEvent.class)
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
                            path = "/messages/send-private-message", method = RequestMethod.POST,
                            beanClass = MessageHandler.class, beanMethod = "handleSendMessage",
                            operation = @Operation(
                                    operationId = "handleSendMessage",
                                    summary = "Отправка личного сообщения пользователю",
                                    requestBody = @RequestBody(
                                            description = "Send message request body",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = MessageRequest.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                            schema = @Schema(implementation = String.class)
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
                            path = "/messages/delete-private-message", method = RequestMethod.DELETE,
                            beanClass = MessageHandler.class, beanMethod = "handleDeleteMessage",
                            operation = @Operation(
                                    operationId = "handleDeleteMessage",
                                    summary = "Удаления личного сообщения по идентификатору отправителя",
                                    parameters = @Parameter(
                                            name = "messageId", in = ParameterIn.DEFAULT,
                                            description = "Message id query parameter"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                            schema = @Schema(implementation = String.class)
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
    public RouterFunction<ServerResponse> messages() {
        return RouterFunctions.route()
                .GET("/messages/events", messageHandler::handleServerSendMessageEvent)
                .POST("/messages/send-private-message", messageHandler::handleSendMessage)
                .DELETE("/messages/delete-private-message", messageHandler::handleDeleteMessage)
                .build();
    }
}
