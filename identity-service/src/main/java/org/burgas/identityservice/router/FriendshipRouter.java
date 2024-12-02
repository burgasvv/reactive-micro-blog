package org.burgas.identityservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.FriendshipNotification;
import org.burgas.identityservice.dto.FriendshipRequest;
import org.burgas.identityservice.handler.FriendshipHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class FriendshipRouter {

    private final FriendshipHandler friendshipHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/friendship/by-friend/{friend-id}",
                            beanClass = FriendshipHandler.class, beanMethod = "handleFindFriendshipNotificationsByFriendId",
                            operation = @Operation(
                                    operationId = "handleFindFriendshipNotificationsByFriendId",
                                    summary = "Получить список приглашений в друзья (принятые и не принятые/ true or false)",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "friend-id",
                                                    description = "Friend Id path variable"
                                            ),
                                            @Parameter(in = ParameterIn.PATH, name = "accepted",
                                                    description = "Accepted 'true' or 'false' boolean query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = FriendshipNotification.class)
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
                            path = "/friendship/send-friend-request",
                            beanClass = FriendshipHandler.class, beanMethod = "handleSendFriendRequest",
                            operation = @Operation(
                                    operationId = "handleSendFriendRequestId",
                                    summary = "Отправить заявку в друзья",
                                    requestBody = @RequestBody(
                                            description = "Request body for send friendship notification",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = FriendshipRequest.class)
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
                            path = "/friendship/accept-friendship",
                            beanClass = FriendshipHandler.class, beanMethod = "handleAcceptFriendship",
                            operation = @Operation(
                                    operationId = "handleAcceptFriendshipId",
                                    summary = "Принять заявку в друзья",
                                    requestBody = @RequestBody(
                                            description = "Request body for accept friendship notification",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = FriendshipRequest.class)
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
                            path = "/friendship/decline-friendship",
                            beanClass = FriendshipHandler.class, beanMethod = "handleDeclineFriendship",
                            operation = @Operation(
                                    operationId = "handleDeclineFriendshipId",
                                    summary = "Отклонить заявку в друзья",
                                    requestBody = @RequestBody(
                                            description = "Request body for decline friendship notification",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = FriendshipRequest.class)
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
                            path = "/friendship/delete-from-friendship",
                            beanClass = FriendshipHandler.class, beanMethod = "handleDeleteFriendship",
                            operation = @Operation(
                                    operationId = "handleDeleteFriendshipId",
                                    summary = "Удалить из друзей",
                                    requestBody = @RequestBody(
                                            description = "Request body for decline friendship notification",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = FriendshipRequest.class)
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
                    )
            }
    )
    public RouterFunction<ServerResponse> friendship() {
        return RouterFunctions.route()
                .GET("/friendship/by-friend/{friend-id}", friendshipHandler::handleFindFriendshipNotificationsByFriendId)
                .POST("/friendship/send-friend-request", friendshipHandler::handleSendFriendRequest)
                .POST("/friendship/accept-friendship", friendshipHandler::handleAcceptFriendship)
                .POST("/friendship/decline-friendship", friendshipHandler::handleDeclineFriendship)
                .DELETE("/friendship/delete-from-friendship", friendshipHandler::handleDeleteFriendship)
                .build();
    }
}
