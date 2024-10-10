package study.shinseungyeol.backend.api.reservation.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ConcertReservationDto {

  public record RequestConcertReservation(UUID token, Long seatId) {

  }

  public record ResponseConcertReservation(Long concertId, Long seatId, BigDecimal price) {

  }
}
