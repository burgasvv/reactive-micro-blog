package org.burgas.postservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8765", description = "API Post server"),
        info = @Info(
                title = "API Chat Service", version = "1.0",
                description = "Post service - сервис публикации постов на страницах аккаунтов и сообществах."
        )
)
public class OpenApiConfig {
}
