package vn.utc.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Garage management.")
                        .description("Garage management Open Api.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("DO KHANH LINH")
                                .email("linhdk0712@gmail.com")));
    }


}
