package study.shinseungyeol.backend.api.reservation.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.shinseungyeol.backend.usecase.reservation.dto.ReserveConcertSeat;

public class ReserveConcertSeatAPI {

  @Getter
  @AllArgsConstructor
  public static class Request {

    private UUID token;
    private Long seatId;

    public ReserveConcertSeat.Command toCommand() {
      return new ReserveConcertSeat.Command(token, seatId);
    }

  }

  @Getter
  @AllArgsConstructor
  public static class Response {

    private Long reservationId;

    public static Response of(ReserveConcertSeat.CommandResult reservation) {
      return new Response(reservation.getConcertSeatReservationId());
    }
  }
}
