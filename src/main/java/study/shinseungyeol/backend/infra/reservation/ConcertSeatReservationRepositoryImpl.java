package study.shinseungyeol.backend.infra.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationRepository;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;

@Repository
@RequiredArgsConstructor
public class ConcertSeatReservationRepositoryImpl implements ConcertSeatReservationRepository {

  private final ConcertSeatReservationJPARepository reservationRepository;

  @Override
  public ConcertSeatReservation save(ConcertSeatReservation reservation) {
    return reservationRepository.save(reservation);
  }

  @Override
  public List<ConcertSeatReservation> findAllExpired(ReservationStatus status, LocalDateTime now) {
    return reservationRepository.findAllExpired(status, now);
  }

  @Override
  public Optional<ConcertSeatReservation> findById(Long id) {
    return reservationRepository.findById(id);
  }

  @Override
  public Optional<ConcertSeatReservation> findByIdForUpdate(Long id) {
    return reservationRepository.findByIdForUpdate(id);
  }

  @Override
  public List<ConcertSeatReservation> findAll() {
    return reservationRepository.findAll();
  }
}
