package org.phinxt.navigator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Navigator Application", version = "1.0", description = "Navigator Application"))
public class OpenAPIConfig {
}
