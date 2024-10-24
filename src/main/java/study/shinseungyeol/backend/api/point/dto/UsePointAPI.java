package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.point.dto.UsePoint;

public class UsePointAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private UUID uuid;
    private Long reservationId;

    public UsePoint.Command toCommand() {
      return new UsePoint.Command(uuid, reservationId);
    }
  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long memberId;
    private BigDecimal balance;

    public static Response of(UsePoint.CommandResult commandResult) {
      return new Response(commandResult.getMemberId(), commandResult.getBalance());

    }
  }
}
