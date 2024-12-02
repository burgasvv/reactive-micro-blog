package org.burgas.postservice.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.burgas.postservice.dto.CommentRequest;
import org.burgas.postservice.dto.CommentResponse;
import org.burgas.postservice.handler.CommentHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class CommentRouter {

    private final CommentHandler commentHandler;

    @Bean
    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/comments/in-post", method = RequestMethod.GET,
                            beanClass = CommentHandler.class, beanMethod = "handleFindByPostId",
                            operation = @Operation(
                                    operationId = "handleFindByPostId",
                                    summary = "Получение комментариев к посту по его идентификатору",
                                    parameters = {
                                            @Parameter(
                                                    name = "postId", in = ParameterIn.DEFAULT,
                                                    description = "Post Id query parameter"
                                            )
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommentResponse.class)
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
                            path = "/comments/create", method = RequestMethod.POST,
                            beanClass = CommentHandler.class, beanMethod = "handleCreateOrUpdate",
                            operation = @Operation(
                                    operationId = "handleCreateOrUpdateId",
                                    summary = "Публикация комментария к посту",
                                    requestBody = @RequestBody(
                                            description = "Request body for comment creation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CommentRequest.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommentResponse.class)
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
                            path = "/comments/update", method = RequestMethod.PUT,
                            beanClass = CommentHandler.class, beanMethod = "handleCreateOrUpdate",
                            operation = @Operation(
                                    operationId = "handleCreateOrUpdateId",
                                    summary = "Обновление данных комментария к посту",
                                    requestBody = @RequestBody(
                                            description = "Request body for comment creation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CommentRequest.class)
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200", description = "Successful operation",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = CommentResponse.class)
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
                            path = "/comments/delete", method = RequestMethod.DELETE,
                            beanClass = CommentHandler.class, beanMethod = "handleDelete",
                            operation = @Operation(
                                    operationId = "handleDeleteId",
                                    summary = "Удаление комментария к посту по его идентификатору",
                                    parameters = {
                                            @Parameter(
                                                    name = "commentId", in = ParameterIn.DEFAULT,
                                                    description = "Comment Id query parameter"
                                            ),
                                            @Parameter(
                                                    name = "identityId", in = ParameterIn.DEFAULT,
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
    public RouterFunction<ServerResponse> comments() {
        return RouterFunctions.route()
                .GET("/comments/in-post", commentHandler::handleFindByPostId)
                .POST("/comments/create", commentHandler::handleCreateOrUpdate)
                .PUT("/comments/update", commentHandler::handleCreateOrUpdate)
                .DELETE("/comments/delete", commentHandler::handleDelete)
                .build();
    }
}
