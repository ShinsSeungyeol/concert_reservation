package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.point.dto.ChargePoint;

public class ChargePointAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private UUID token;
    private BigDecimal chargingAmount;

    public ChargePoint.Command toCommand() {
      return new ChargePoint.Command(token, chargingAmount);
    }
  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long memberId;
    private BigDecimal balance;

    public static Response of(ChargePoint.CommandResult commandResult) {
      return new Response(commandResult.getMemberId(), commandResult.getBalance());
    }
  }
}
