package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ChargingPointDto {

  public record RequestChargingPoint(UUID token, BigDecimal chargingAmount) {

  }

  public record ResponseChargingPoint(BigDecimal balanceAmount) {


  }
}
