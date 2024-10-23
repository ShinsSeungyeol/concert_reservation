package study.shinseungyeol.backend.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertSeatReservationRepository {

  ConcertSeatReservation save(ConcertSeatReservation concertSeatReservation);

  List<ConcertSeatReservation> findAllExpired(ReservationStatus status, LocalDateTime now);

  Optional<ConcertSeatReservation> findById(Long id);

  Optional<ConcertSeatReservation> findByIdForUpdate(Long id);

  List<ConcertSeatReservation> findAll();

}
