package org.burgas.identityservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.IdentityRequestCreate;
import org.burgas.identityservice.dto.IdentityRequestUpdate;
import org.burgas.identityservice.dto.IdentityResponse;
import org.burgas.identityservice.handler.IdentityHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class IdentityRouter {

    private final IdentityHandler identityHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/identities",
                            beanClass = IdentityHandler.class, beanMethod = "handleFindAll",
                            operation = @Operation(
                                    operationId = "findIdentities", summary = "Получить список аккаунтов",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/by-id/{identity-id}",
                            beanClass = IdentityHandler.class, beanMethod = "handleFindById",
                            operation = @Operation(
                                    operationId = "findIdentityById", summary = "Получить аккаунт по идентификатору",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "identity-id",
                                                    description = "Identity Id path variable"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/by-id/{identity-id}/friends",
                            beanClass = IdentityHandler.class, beanMethod = "handleFindFriendsByIdentityId",
                            operation = @Operation(
                                    operationId = "findIdentityById", summary = "Получить список друзей по идентификатору аккаунта",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "identity-id",
                                                    description = "Identity Id path variable"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/in-community",
                            beanClass = IdentityHandler.class, beanMethod = "handleFindIdentitiesByCommunityId",
                            operation = @Operation(
                                    operationId = "findIdentitiesInCommunityId",
                                    summary = "Получить список аккаунтов, состоящих в сообществе",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "identityId",
                                                    description = "Identity Id query parameter"
                                            ),
                                            @Parameter(
                                                    in = ParameterIn.DEFAULT, name = "communityId",
                                                    description = "Community Id query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/{username}",
                            beanClass = IdentityHandler.class, beanMethod = "handleFindByUsername",
                            operation = @Operation(
                                    operationId = "аштвШвутешенИнГыуктфьу", summary = "Получить аккаунт по username пользователя",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "username",
                                                    description = "Username path variable"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/create",
                            beanClass = IdentityHandler.class, beanMethod = "handleCreateIdentity",
                            operation = @Operation(
                                    operationId = "createIdentity", summary = "Создать аккаунт пользователя",
                                    requestBody = @RequestBody(
                                            description = "Request body for create identity method",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityRequestCreate.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/create-wall",
                            beanClass = IdentityHandler.class, beanMethod = "handleCreateIdentityWall",
                            operation = @Operation(
                                    operationId = "updateIdentity", summary = "Создать стену аккаунта",
                                    requestBody = @RequestBody(
                                            description = "Request body for create identity wall",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityRequestCreate.class)
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
                            path = "/identities/update",
                            beanClass = IdentityHandler.class, beanMethod = "handleUpdateIdentity",
                            operation = @Operation(
                                    operationId = "updateIdentity", summary = "Обновить данные аккаунта пользователя",
                                    requestBody = @RequestBody(
                                            description = "Request body for create identity method",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = IdentityRequestUpdate.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = IdentityResponse.class)
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
                            path = "/identities/delete/{identity-id}",
                            beanClass = IdentityHandler.class, beanMethod = "handleDeleteIdentity",
                            operation = @Operation(
                                    operationId = "deleteIdentityById", summary = "Удалить аккаунт по идентификатору",
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "identity-id",
                                                    description = "Identity Id path variable"
                                            )
                                    },
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
    public RouterFunction<ServerResponse> identityRouterFunction() {
        return RouterFunctions.route()
                .GET("/identities", identityHandler::handleFindAll)
                .GET("/identities/by-id/{identity-id}", identityHandler::handleFindById)
                .GET("/identities/by-id/{identity-id}/friends", identityHandler::handleFindFriendsByIdentityId)
                .GET("/identities/in-community", identityHandler::handleFindIdentitiesByCommunityId)
                .GET("/identities/{username}", identityHandler::handleFindByUsername)
                .POST("/identities/create", identityHandler::handleCreateIdentity)
                .POST("/identities/create-wall", identityHandler::handleCreateIdentityWall)
                .PUT("/identities/update", identityHandler::handleUpdateIdentity)
                .DELETE("/identities/delete/{identity-id}", identityHandler::handleDeleteIdentity)
                .build();
    }
}
