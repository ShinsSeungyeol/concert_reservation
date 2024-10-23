package study.shinseungyeol.backend.usecase.concert.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;

public class AvailableConcertSchedule {

  @Getter
  @AllArgsConstructor
  public static class Query {

    private UUID uuid;
    private Long concertID;
  }

  @Getter
  @AllArgsConstructor
  public static class QueryResult {

    private Long concertId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static QueryResult of(ConcertSchedule concertSchedule) {
      return new QueryResult(concertSchedule.getId(), concertSchedule.getStartAt(),
          concertSchedule.getEndAt());

    }
  }

}
