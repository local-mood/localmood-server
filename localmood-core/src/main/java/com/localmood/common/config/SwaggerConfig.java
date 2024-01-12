package com.localmood.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    Info info = new Info()
      .title("Local Mood API")
      .description("로컬무드 API")
      .version("0.0.1");

    SecurityScheme basicAuth = new SecurityScheme()
      .type(Type.HTTP)
      .scheme("Bearer")
      .bearerFormat("JWT")
      .in(In.HEADER)
      .name("Authorization");

    SecurityRequirement securityItem = new SecurityRequirement().addList("basicAuth");

    return new OpenAPI()
      .components(new Components().addSecuritySchemes("basicAuth", basicAuth))
      .addSecurityItem(securityItem)
      .info(info);

  }
}
