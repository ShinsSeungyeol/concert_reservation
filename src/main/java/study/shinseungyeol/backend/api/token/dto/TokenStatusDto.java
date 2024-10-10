package study.shinseungyeol.backend.api.token.dto;

import java.util.UUID;

public class TokenStatusDto {

  public record RequestTokenStatus(UUID token) {

  }

  public record ResponseTokenStatus(UUID token, String tokenStatus) {

  }
}
