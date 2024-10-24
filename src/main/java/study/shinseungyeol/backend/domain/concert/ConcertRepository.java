package study.shinseungyeol.backend.domain.concert;

import java.util.Optional;

public interface ConcertRepository {

  Optional<Concert> findById(Long id);

  Concert save(Concert concert);

  void deleteAll();
}
