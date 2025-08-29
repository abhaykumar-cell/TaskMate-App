package com.abhay.Config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI().info(
                new Info()
                        .title("TaskMate App APIs")
                        .description("APIs for TaskMate application")
                        .version("1.0.0")
        ).tags(Arrays.asList(
                new Tag().name("Authentication APIs"),
                new Tag().name("Task APIs")
        ));

    }
}
