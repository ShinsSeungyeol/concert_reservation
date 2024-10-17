package study.shinseungyeol.backend.api.concert.dto;

import java.util.List;
import java.util.UUID;

public class AvailableConcertSeatsDto {

  public record RequestAvailableConcertSeats(Long concertId, UUID token) {

  }

  public record ResponseAvailableConcertSeats(Long concertId, List<Long> availableConcertSeatIds) {

  }

}
