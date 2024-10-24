package study.shinseungyeol.backend.api.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import study.shinseungyeol.backend.domain.token.TokenService;

@Component
@RequiredArgsConstructor
public class TokenValidatorInterceptor implements HandlerInterceptor {

  private final TokenService tokenService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    UUID uuidFromBody = extractTokenFromBody(request);
    UUID uuidFromParam = extractTokenFromParam(request);

    if (uuidFromBody == null && uuidFromParam == null) {
      return false;
    }

    if (uuidFromBody != null) {
      tokenService.getTokenWithValidateActive(uuidFromBody);
    }

    if (uuidFromParam != null) {
      tokenService.getTokenWithValidateActive(uuidFromParam);
    }

    return true;
  }

  private UUID extractTokenFromBody(HttpServletRequest request) throws Exception {
    if (request instanceof ContentCachingRequestWrapper wrapper) {
      String requestBody = new String(wrapper.getContentAsByteArray(),
          request.getCharacterEncoding());

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(requestBody);

      if (jsonNode.has("uuid")) {
        return UUID.fromString(jsonNode.get("uuid").asText());
      }
      return null;
    }

    return null;
  }

  private UUID extractTokenFromParam(HttpServletRequest request) throws Exception {
    return UUID.fromString(request.getParameter("uuid"));
  }


}
