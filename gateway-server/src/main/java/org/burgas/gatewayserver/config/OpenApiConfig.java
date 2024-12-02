package org.burgas.gatewayserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8765", description = "API Gateway server"),
        info = @Info(
                title = "API Gateway service", version = "1.0",
                description = "Gateway Server - сервер для перенаправления, фильтрации и балансировки запросов от пользователей" +
                              " между сервисами в системе, а также организует базовую авторизацию и аутентификацию пользователя."
        )
)
public class OpenApiConfig {
}
