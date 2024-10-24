package study.shinseungyeol.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.shinseungyeol.backend.api.interceptor.TokenValidatorInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final TokenValidatorInterceptor tokenValidInterceptor;

  @Bean
  public HttpMessageConverter<Object> createJsonMessageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    return new MappingJackson2HttpMessageConverter(objectMapper);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(tokenValidInterceptor)
        .addPathPatterns("/api/v1/concert/**")
        .addPathPatterns("/api/v1/point/**")
        .addPathPatterns("/api/v1/reservation/**");
  }
}
