package study.shinseungyeol.backend.usecase.concert.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;

public class AvailableConcertSeat {

  @Getter
  @AllArgsConstructor
  public static class Query {

    private UUID uuid;
    private Long concertId;
  }

  @Getter
  @AllArgsConstructor
  public static class QueryResult {

    private Long concertSeatId;

    public static QueryResult of(ConcertSeat concertSeat) {
      return new QueryResult(concertSeat.getId());
    }
  }

}
