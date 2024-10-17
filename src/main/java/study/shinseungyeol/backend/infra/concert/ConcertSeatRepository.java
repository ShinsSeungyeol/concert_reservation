package study.shinseungyeol.backend.infra.concert;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

  List<ConcertSeat> findAllByConcertSchedule_ConcertAndAvailableIsTrue(Concert concert);

  Long countAllByConcertScheduleAndAvailableIsTrue(ConcertSchedule concertSchedule);

  @Query("SELECT c FROM ConcertSeat c WHERE c.id = :concertSeatId")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ConcertSeat> findByIdForUpdate(@Param("concertSeatId") Long concertSeatId);
}
