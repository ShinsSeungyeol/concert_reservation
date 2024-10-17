package study.shinseungyeol.backend.api.token.dto;

import java.util.UUID;
import study.shinseungyeol.backend.domain.token.Token;

public class TokenStatusDto {

  public record ResponseTokenStatus(UUID token, String tokenStatus) {

    public static ResponseTokenStatus of(Token token) {
      return new ResponseTokenStatus(token.getId(), token.getStatus().toString());
    }

  }
}
