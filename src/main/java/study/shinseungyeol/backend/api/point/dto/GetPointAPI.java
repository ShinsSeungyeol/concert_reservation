package study.shinseungyeol.backend.api.point.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.point.dto.GetPoint;

public class GetPointAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private UUID uuid;


  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long memberId;
    private BigDecimal balance;

    public static Response of(GetPoint.QueryResult queryResult) {
      return new Response(queryResult.getMemberId(), queryResult.getBalance());
    }
  }
}
