package study.shinseungyeol.backend.api.concert.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSeat.Query;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSeat.QueryResult;

public class AvailableConcertSeatAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private Long concertId;
    private UUID token;

    public Query toQuery() {
      return new Query(token, concertId);
    }
  }


  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long availableConcertSeatId;

    public static Response of(QueryResult queryResult) {
      return new Response(queryResult.getConcertSeatId());
    }

  }
}
