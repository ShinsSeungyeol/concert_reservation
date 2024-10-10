package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class UsePointDto {

  public record RequestUsePoint(UUID token, BigDecimal useAmount) {

  }

  public record ResponseUsePoint(Long memberId, BigDecimal balanceAmount) {

  }
}
