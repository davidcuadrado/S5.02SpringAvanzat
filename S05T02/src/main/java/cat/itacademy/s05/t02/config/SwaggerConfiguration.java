package cat.itacademy.s05.t02.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("pet-api")
                .pathsToMatch("/user/**", "/pet/**", "/app")
                .build();
    }
}