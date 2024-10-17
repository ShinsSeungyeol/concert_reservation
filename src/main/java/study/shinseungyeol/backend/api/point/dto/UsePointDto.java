package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class UsePointDto {

  public record RequestUsePoint(UUID token, Long reservationId) {

  }

  public record ResponseUsePoint(Long reservationId, BigDecimal balanceAmount) {

  }
}
