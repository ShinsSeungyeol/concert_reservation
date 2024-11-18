package study.shinseungyeol.backend.usecase.reservation.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import study.shinseungyeol.backend.usecase.platform.DataPlatformService;

@Component
@RequiredArgsConstructor
public class ConcertSeatReservedEventListener {

  private final DataPlatformService dataPlatformService;


  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void concertSeatReservedEventHandler(ConcertSeatReservedEvent event) {
    ConcertSeatReservationPayload concertSeatReservationPayload = ConcertSeatReservationPayload.of(
        event);

    dataPlatformService.send(concertSeatReservationPayload);

  }

}
