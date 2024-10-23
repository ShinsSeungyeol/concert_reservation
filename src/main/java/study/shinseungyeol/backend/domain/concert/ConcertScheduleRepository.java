package study.shinseungyeol.backend.domain.concert;

import java.util.List;

public interface ConcertScheduleRepository {

  ConcertSchedule save(ConcertSchedule concertSchedule);

  List<ConcertSchedule> findAllByConcert(Concert concert);
}
