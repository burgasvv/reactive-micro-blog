package org.burgas.communityservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8765", description = "API Community Server"),
        info = @Info(
                title = "API Community Service", version = "1.0",
                description = "Community Service - сервис организации сообществ пользователей"
        )
)
public class OpenApiConfig {
}
