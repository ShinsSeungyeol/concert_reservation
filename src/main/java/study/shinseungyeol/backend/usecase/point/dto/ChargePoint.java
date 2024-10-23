package study.shinseungyeol.backend.usecase.point.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.domain.point.Point;

public class ChargePoint {

  @Getter
  @AllArgsConstructor
  public static class Command {

    private UUID uuid;
    private BigDecimal chargingAmount;
  }

  @Getter
  @AllArgsConstructor
  public static class CommandResult {

    private Long memberId;
    private BigDecimal balance;

    public static CommandResult of(Point point) {
      return new CommandResult(point.getMemberId(), point.getBalanceAmount());
    }
  }

}
