package org.burgas.communityservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.CommunityRequest;
import org.burgas.communityservice.dto.CommunityResponse;
import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.burgas.communityservice.dto.IdentityCommunityRequest;
import org.burgas.communityservice.handler.CommunityHandler;
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
public class CommunityRouter {

    private final CommunityHandler communityHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/communities/get-not-accepted-notifications-for-receiver", method = RequestMethod.GET,
                            beanClass = CommunityHandler.class, beanMethod = "handleGetNotAcceptedNotificationsByReceiver",
                            operation = @Operation(
                                    operationId = "handleGetNotAcceptedNotificationsByReceiver",
                                    summary = "Получение не прочитанных оповещений-приглашений в сообщество",
                                    parameters = @Parameter(
                                            name = "receiverId", in = ParameterIn.DEFAULT,
                                            description = "Receiver id query parameter"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityCommunityNotification.class)
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
                            path = "/communities/get-accepted-notifications-for-receiver", method = RequestMethod.GET,
                            beanClass = CommunityHandler.class, beanMethod = "handleGetAcceptedNotificationsByReceiver",
                            operation = @Operation(
                                    operationId = "handleGetAcceptedNotificationsByReceiver",
                                    summary = "Получение прочитанных оповещений-приглашений в сообщество",
                                    parameters = @Parameter(
                                            name = "receiverId", in = ParameterIn.DEFAULT,
                                            description = "Receiver id query parameter"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityCommunityNotification.class)
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
                            path = "/communities/{community-id}", method = RequestMethod.GET,
                            beanClass = CommunityHandler.class, beanMethod = "handleFindById",
                            operation = @Operation(
                                    operationId = "handleFindById",
                                    summary = "Получение сообщества по идентификатору",
                                    parameters = @Parameter(
                                            name = "community-id", in = ParameterIn.PATH,
                                            description = "Receiver id path variable"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommunityResponse.class)
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
                            path = "/communities/on-identity/{identity-id}", method = RequestMethod.GET,
                            beanClass = CommunityHandler.class, beanMethod = "handleFindCommunitiesByIdentityId",
                            operation = @Operation(
                                    operationId = "handleFindById",
                                    summary = "Получение списка сообществ по идентификатору пользователя",
                                    parameters = @Parameter(
                                            name = "identity-id", in = ParameterIn.PATH,
                                            description = "Identity id path variable"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommunityResponse.class)
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
                            path = "/communities/create-wall", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleCreateCommunityWallByCommunityId",
                            operation = @Operation(
                                    operationId = "handleCreateCommunityWallByCommunityId",
                                    summary = "Создать стену постов в сообществе",
                                    requestBody = @RequestBody(
                                            description = "Request body for community wall creation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CommunityRequest.class)
                                            )
                                    ),
                                    parameters = @Parameter(
                                            name = "identityId", in = ParameterIn.DEFAULT,
                                            description = "Identity id query parameter"
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
                            path = "/communities/create", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleCreateCommunity",
                            operation = @Operation(
                                    operationId = "handleCreateCommunity",
                                    summary = "Создать сообщество",
                                    requestBody = @RequestBody(
                                            description = "Request body for community wall creation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CommunityRequest.class)
                                            )
                                    ),
                                    parameters = @Parameter(
                                            name = "identityId", in = ParameterIn.DEFAULT,
                                            description = "Identity id query parameter"
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommunityResponse.class)
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
                            path = "/communities/join-the-community", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleJoinTheCommunity",
                            operation = @Operation(
                                    operationId = "handleJoinTheCommunity",
                                    summary = "Вступить в сообщество",
                                    requestBody = @RequestBody(
                                            description = "Request body for joining to community",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityCommunityRequest.class)
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
                            path = "/communities/leave-the-community", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleLeaveTheCommunity",
                            operation = @Operation(
                                    operationId = "handleLeaveTheCommunity",
                                    summary = "Выйти из сообщества",
                                    requestBody = @RequestBody(
                                            description = "Request body for leaving from community",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityCommunityRequest.class)
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
                            path = "/communities/send-invitation-community-administration", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleSendInvitationToCommunityAdministration",
                            operation = @Operation(
                                    operationId = "handleSendInvitationToCommunityAdministration",
                                    summary = "Предложить администрирование в сообществе",
                                    requestBody = @RequestBody(
                                            description = "Request body for sending administration",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityCommunityRequest.class)
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
                            path = "/communities/accept-invitation-community-administration", method = RequestMethod.POST,
                            beanClass = CommunityHandler.class, beanMethod = "handleAcceptInvitationToCommunityAdministration",
                            operation = @Operation(
                                    operationId = "handleAcceptInvitationToCommunityAdministration",
                                    summary = "Принять предложение об администрировании в сообществе",
                                    requestBody = @RequestBody(
                                            description = "Request body for accepting administration",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityCommunityRequest.class)
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
                            path = "/communities/decline-invitation-community-administration", method = RequestMethod.DELETE,
                            beanClass = CommunityHandler.class, beanMethod = "handleDeclineInvitationToCommunityAdministration",
                            operation = @Operation(
                                    operationId = "handleDeclineInvitationToCommunityAdministration",
                                    summary = "Отклонить предложение об администрировании в сообществе",
                                    requestBody = @RequestBody(
                                            description = "Request body for declining administration",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityCommunityRequest.class)
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
    public RouterFunction<ServerResponse> communities() {
        return RouterFunctions.route()
                .GET(
                        "/communities/get-not-accepted-notifications-for-receiver",
                        communityHandler::handleGetNotAcceptedNotificationsByReceiver
                )
                .GET(
                        "/communities/get-accepted-notifications-for-receiver",
                        communityHandler::handleGetAcceptedNotificationsByReceiver
                )
                .GET("/communities/{community-id}", communityHandler::handleFindById)
                .GET("/communities/on-identity/{identity-id}", communityHandler::handleFindCommunitiesByIdentityId)
                .POST("/communities/create-wall", communityHandler::handleCreateCommunityWallByCommunityId)
                .POST("/communities/create", communityHandler::handleCreateCommunity)
                .POST("/communities/join-the-community", communityHandler::handleJoinTheCommunity)
                .POST("/communities/leave-the-community", communityHandler::handleLeaveTheCommunity)
                .POST(
                        "/communities/send-invitation-community-administration",
                        communityHandler::handleSendInvitationToCommunityAdministration)
                .POST(
                        "/communities/accept-invitation-community-administration",
                        communityHandler::handleAcceptInvitationToCommunityAdministration
                )
                .DELETE(
                        "/communities/decline-invitation-community-administration",
                        communityHandler::handleDeclineInvitationToCommunityAdministration
                )
                .build();
    }
}
