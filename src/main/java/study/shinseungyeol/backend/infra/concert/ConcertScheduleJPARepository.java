package study.shinseungyeol.backend.infra.concert;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;

@Repository
public interface ConcertScheduleJPARepository extends JpaRepository<ConcertSchedule, Long> {

  List<ConcertSchedule> findAllByConcert(Concert concert);

}
