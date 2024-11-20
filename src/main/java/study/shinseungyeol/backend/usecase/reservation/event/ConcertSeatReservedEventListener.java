package study.shinseungyeol.backend.usecase.reservation.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import study.shinseungyeol.backend.infra.kafka.KafkaProducer;

@Component
@RequiredArgsConstructor
public class ConcertSeatReservedEventListener {

  private final KafkaProducer kafkaProducer;


  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void concertSeatReservedEventHandler(ConcertSeatReservedEvent event)
      throws JsonProcessingException {
    ConcertSeatReservationPayload concertSeatReservationPayload = ConcertSeatReservationPayload.of(
        event);

    kafkaProducer.send(concertSeatReservationPayload);
  }
}
