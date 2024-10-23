package study.shinseungyeol.backend.usecase.reservation.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;

public class ReserveConcertSeat {

  @Getter
  @AllArgsConstructor
  public static class Command {

    private UUID uuid;
    private Long concertSeatId;
  }

  @Getter
  @AllArgsConstructor
  public static class CommandResult {

    private Long concertSeatReservationId;

    public static CommandResult of(ConcertSeatReservation concertSeatReservation) {
      return new CommandResult(concertSeatReservation.getId());
    }
  }
}
