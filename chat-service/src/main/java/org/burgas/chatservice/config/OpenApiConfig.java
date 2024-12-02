package org.burgas.chatservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8765", description = "API Chat Server"),
        info = @Info(
                title = "API Chat Service", version = "1.0",
                description = "ChatService - сервис обмена личными сообщениями между пользователя"
        )
)
public class OpenApiConfig {
}
