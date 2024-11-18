package study.shinseungyeol.backend.usecase.reservation.event;

import lombok.Getter;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;

@Getter
public class ConcertSeatReservationPayload {

  private Long seatId;
  private Long memberId;
  private Long reservationId;
  private ReservationStatus status;

  public static ConcertSeatReservationPayload of(ConcertSeatReservedEvent event) {
    ConcertSeatReservationPayload payload = new ConcertSeatReservationPayload();
    payload.seatId = event.getSeatId();
    payload.memberId = event.getMemberId();
    payload.reservationId = event.getReservationId();
    payload.status = ReservationStatus.COMPLETED;

    return payload;
  }

  @Override
  public String toString() {
    return "ConcertSeatReservationPayload{" +
        "seatId=" + seatId +
        ", memberId=" + memberId +
        ", reservationId=" + reservationId +
        ", status=" + status +
        '}';
  }
}
