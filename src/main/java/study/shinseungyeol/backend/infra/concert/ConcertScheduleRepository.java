package study.shinseungyeol.backend.infra.concert;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;

public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {

  List<ConcertSchedule> findAllByConcert(Concert concert);

}
