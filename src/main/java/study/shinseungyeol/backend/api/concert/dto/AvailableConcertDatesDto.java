package study.shinseungyeol.backend.api.concert.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AvailableConcertDatesDto {

  public record RequestAvailableConcertDates(UUID token, Long concertId) {

  }

  public record ResponseAvailableConcertDates(List<AvailableConcertDate> reservationDates) {

  }

  public record AvailableConcertDate(LocalDateTime start, LocalDateTime end, Long concertId) {

  }
}
