package G5_SWP391.ChildGrownTracking.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

  //http://localhost:8080/swagger-ui.html
@Configuration
public class SwaggerConfig {
      @Bean
      public OpenAPI customOpenAPI() {
          return new OpenAPI()
                  .info(new Info()
                          .title("Child Growth Tracking API")
                          .version("1.0")
                          .description("API documentation for Child Growth Tracking application"))
                  .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                  .components(new Components()
                          .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                  .type(SecurityScheme.Type.HTTP)
                                  .scheme("bearer")
                                  .bearerFormat("JWT")));
      }

}
