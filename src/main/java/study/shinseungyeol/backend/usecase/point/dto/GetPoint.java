package study.shinseungyeol.backend.usecase.point.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.domain.point.Point;

public class GetPoint {

  @Getter
  @AllArgsConstructor
  public static class Query {

    private UUID uuid;
  }

  @Getter
  @AllArgsConstructor
  public static class QueryResult {

    private Long memberId;
    private BigDecimal balance;

    public static QueryResult of(Point point) {
      return new QueryResult(point.getMemberId(), point.getBalanceAmount());
    }
  }
}
