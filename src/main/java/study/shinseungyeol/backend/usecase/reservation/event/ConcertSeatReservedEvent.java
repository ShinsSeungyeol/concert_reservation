package study.shinseungyeol.backend.usecase.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConcertSeatReservedEvent {

  private Long memberId;
  private Long seatId;
  private Long reservationId;
}
