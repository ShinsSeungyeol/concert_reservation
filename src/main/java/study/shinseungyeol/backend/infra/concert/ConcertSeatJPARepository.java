package study.shinseungyeol.backend.infra.concert;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;

@Repository
public interface ConcertSeatJPARepository extends JpaRepository<ConcertSeat, Long> {

  @Query("SELECT cs FROM ConcertSeat cs WHERE cs.concertSchedule.concert = :concert AND cs.available = true")
  List<ConcertSeat> findAllAvailableConcertSeats(@Param("concert") Concert concert);

  @Query("SELECT COUNT(cs) FROM ConcertSeat cs WHERE cs.concertSchedule = :concertSchedule AND cs.available = true")
  Long countAllAvailableConcertSeats(
      @Param("concertSchedule") ConcertSchedule concertSchedule);

  @Query("SELECT c FROM ConcertSeat c WHERE c.id = :concertSeatId")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ConcertSeat> findByIdForUpdate(@Param("concertSeatId") Long concertSeatId);
}
