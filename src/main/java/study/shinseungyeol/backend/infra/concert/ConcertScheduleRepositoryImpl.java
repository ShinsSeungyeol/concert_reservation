package study.shinseungyeol.backend.infra.concert;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertScheduleRepository;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

  private final ConcertScheduleJPARepository concertScheduleJPARepository;

  @Override
  public ConcertSchedule save(ConcertSchedule concertSchedule) {
    return concertScheduleJPARepository.save(concertSchedule);
  }

  @Override
  public List<ConcertSchedule> findAllByConcert(Concert concert) {
    return concertScheduleJPARepository.findAllByConcert(concert);
  }

  @Override
  public void deleteAll() {
    concertScheduleJPARepository.deleteAll();
  }


}
