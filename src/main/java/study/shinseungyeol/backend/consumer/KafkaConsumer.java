package study.shinseungyeol.backend.consumer;

import java.io.IOException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

  @KafkaListener(topics = "concert-seat-reservation", groupId = "reservation-group")
  public void consume(String message) throws IOException {
    System.out.printf("Consumed message : %s%n", message);
  }

}
