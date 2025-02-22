package G5_SWP391.ChildGrownTracking.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .description("API for managing child growth tracking system"));
    }
}
