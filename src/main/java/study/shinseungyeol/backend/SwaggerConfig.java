package study.shinseungyeol.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "항해 콘서트 예약 API Doc",
        description = "콘서트 예약 API 명세 입니다.",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {

}
