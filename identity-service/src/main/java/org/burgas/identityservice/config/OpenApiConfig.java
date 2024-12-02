package org.burgas.identityservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8765", description = "Gateway Server"),
        info = @Info(
                title = "API Identity Service", version = "1.0",
                description = "Identity Service - сервис для управления, настройки " +
                              "и организации аккаунтов пользователей и администраторов"
        )
)
public class OpenApiConfig {
}
