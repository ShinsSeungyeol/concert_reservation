package study.shinseungyeol.backend.api.token.dto;

import java.util.UUID;

public class CreateTokenDto {

  public record RequestCreateToken(Long memberId) {

  }

  public record ResponseCreateToken(UUID uuid) {

  }
}
