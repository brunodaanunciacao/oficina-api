package br.com.fiap.oficina.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI oficinaOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Oficina API")
                                .description(
                                        "API REST para gerenciamento de oficina mecânica, " +
                                                "clientes, veículos, serviços, peças, estoque " +
                                                "e ordens de serviço."
                                )
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("FIAP - Oficina MVP")
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}