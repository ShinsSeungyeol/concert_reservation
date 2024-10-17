package study.shinseungyeol.backend.infra.reservation;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;

@Repository
public interface ConcertSeatReservationRepository extends
    JpaRepository<ConcertSeatReservation, Long> {

  @Query("SELECT c FROM ConcertSeatReservation c WHERE c.reservationStatus = :status and c.expireAt < :now ")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<ConcertSeatReservation> findExpiredAll(LocalDateTime now);

  @Query("SELECT c FROM ConcertSeatReservation c WHERE c.id = :id")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ConcertSeatReservation> findByIdForUpdate(Long id);

}