package study.shinseungyeol.backend.infra.concert;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertRepository;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

  private final ConcertJPARepository concertJPARepository;

  @Override
  public Optional<Concert> findById(Long id) {
    return concertJPARepository.findById(id);
  }

  @Override
  public Concert save(Concert concert) {
    return concertJPARepository.save(concert);
  }


}
