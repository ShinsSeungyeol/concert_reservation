package study.shinseungyeol.backend.api.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

public class PointDto {

  public record RequestPoint(@Schema(description = "토큰") UUID token) {

  }

  public record ResponsePoint(BigDecimal pointAmount) {

  }

}
