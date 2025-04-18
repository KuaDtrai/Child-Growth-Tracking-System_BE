package G5_SWP391.ChildGrownTracking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

  //http://localhost:8080/swagger-ui.html
  @Configuration
  public class SwaggerConfig {
      @Bean
      public OpenAPI openAPI() {
          return new OpenAPI()
                  .info(new Info()
                          .title("Child Growth Tracking System API")
                          .version("1.0")
                          .description("API documentation for the project"));
                //   .servers(List.of(
                //           new Server().url("https://childgrowthtrackingsystembe-production.up.railway.app/")
                //   ));
      }
  }