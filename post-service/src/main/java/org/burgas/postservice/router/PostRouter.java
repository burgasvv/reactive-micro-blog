package org.burgas.postservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.PostRequest;
import org.burgas.postservice.dto.PostResponse;
import org.burgas.postservice.handler.PostHandler;
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
public class PostRouter {

    private final PostHandler postHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/posts/by-identity", method = RequestMethod.GET,
                            beanClass = PostHandler.class, beanMethod = "handleFindByIdentityId",
                            operation = @Operation(
                                    operationId = "handleFindByIdentityId",
                                    summary = "Получить посты опубликованные пользователем",
                                    parameters = {
                                            @Parameter(
                                                    in = ParameterIn.DEFAULT, name = "identityId",
                                                    description = "Identity Id query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = PostResponse.class)
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
                            path = "/posts/on-wall", method = RequestMethod.GET,
                            beanClass = PostHandler.class, beanMethod = "handleFindByWallId",
                            operation = @Operation(
                                    operationId = "handleFindByWallId",
                                    summary = "Получить посты на стене",
                                    parameters = {
                                            @Parameter(
                                                    in = ParameterIn.DEFAULT, name = "wallId",
                                                    description = "Wall Id query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = PostResponse.class)
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
                            path = "/posts/create", method = RequestMethod.POST,
                            beanClass = PostHandler.class, beanMethod = "handleCreateOrUpdate",
                            operation = @Operation(
                                    operationId = "handleCreatePostId", summary = "Опубликовать пост",
                                    requestBody = @RequestBody(
                                            description = "Request body for post creation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = PostRequest.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = PostResponse.class)
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
                            path = "/posts/update", method = RequestMethod.PUT,
                            beanClass = PostHandler.class, beanMethod = "handleCreateOrUpdate",
                            operation = @Operation(
                                    operationId = "handleUpdatePostId", summary = "Обновить данные поста",
                                    requestBody = @RequestBody(
                                            description = "Request body for post update",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = PostRequest.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = PostResponse.class)
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
                            path = "/posts/delete", method = RequestMethod.DELETE,
                            beanClass = PostHandler.class, beanMethod = "handleDelete",
                            operation = @Operation(
                                    operationId = "handleDeletePostId",
                                    summary = "Удалить пост",
                                    parameters = {
                                            @Parameter(
                                                    in = ParameterIn.DEFAULT, name = "postId",
                                                    description = "Post Id query parameter"
                                            ),
                                            @Parameter(
                                                    in = ParameterIn.DEFAULT, name = "identityId",
                                                    description = "Identity Id query parameter"
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
    public RouterFunction<ServerResponse> posts() {
        return RouterFunctions.route()
                .GET("/posts/by-identity", postHandler::handleFindByIdentityId)
                .GET("/posts/on-wall", postHandler::handleFindByWallId)
                .POST("/posts/create", postHandler::handleCreateOrUpdate)
                .PUT("/posts/update", postHandler::handleCreateOrUpdate)
                .DELETE("/posts/delete", postHandler::handleDelete)
                .build();
    }
}
