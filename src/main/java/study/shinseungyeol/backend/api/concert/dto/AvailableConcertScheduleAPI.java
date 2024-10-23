package study.shinseungyeol.backend.api.concert.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSchedule;

public class AvailableConcertScheduleAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private UUID token;
    private Long concertId;

    public AvailableConcertSchedule.Query toQuery() {
      return new AvailableConcertSchedule.Query(this.getToken(), this.getConcertId());
    }

  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long concertId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static Response of(AvailableConcertSchedule.QueryResult availableConcertSchedule) {
      return new Response(availableConcertSchedule.getConcertId(),
          availableConcertSchedule.getStartAt(), availableConcertSchedule.getEndAt());
    }

  }
}
