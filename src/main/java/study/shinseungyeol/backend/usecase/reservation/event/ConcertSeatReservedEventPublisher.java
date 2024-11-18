package study.shinseungyeol.backend.usecase.reservation.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertSeatReservedEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void raise(ConcertSeatReservedEvent concertSeatReservedEvent) {
    eventPublisher.publishEvent(concertSeatReservedEvent);
  }
}
