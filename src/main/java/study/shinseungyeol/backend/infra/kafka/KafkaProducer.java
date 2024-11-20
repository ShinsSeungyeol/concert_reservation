package study.shinseungyeol.backend.infra.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.usecase.reservation.event.ConcertSeatReservationPayload;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

  private final KafkaTemplate<Integer, String> kafkaTemplate;
  private final String topicName = "concert-seat-reservation";
  private final ObjectMapper objectMapper = new ObjectMapper();




  public void send(ConcertSeatReservationPayload payload) throws JsonProcessingException {

    int hashCode = payload.hashCode();
    this.kafkaTemplate.send(topicName, hashCode % 3, objectMapper.writeValueAsString(payload));

    // 메세지 키 ... 해시함수를 돌려준다... 특정 파티션에만 꽂아 넣을 수 있다.

    log.info(payload.toString());
    log.info("sending....");
    log.info("complete....");

  }
}
