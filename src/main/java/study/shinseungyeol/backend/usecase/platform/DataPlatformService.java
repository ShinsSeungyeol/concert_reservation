package study.shinseungyeol.backend.usecase.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.usecase.reservation.event.ConcertSeatReservationPayload;

@Service
@Slf4j
public class DataPlatformService {

  public void send(ConcertSeatReservationPayload payload) {
    log.info(payload.toString());
    log.info("sending....");
    log.info("complete....");
  }

}
