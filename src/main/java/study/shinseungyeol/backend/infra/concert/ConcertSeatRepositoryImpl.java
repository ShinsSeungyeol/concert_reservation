package study.shinseungyeol.backend.infra.concert;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertSeatRepository;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {

  private final ConcertSeatJPARepository concertSeatJPARepository;

  @Override
  public ConcertSeat save(ConcertSeat concertSeat) {
    return concertSeatJPARepository.save(concertSeat);
  }

  @Override
  public List<ConcertSeat> findAllAvailableSeats(Concert concert) {
    return concertSeatJPARepository.findAllAvailableConcertSeats(concert);
  }

  @Override
  public Long countAllAvailableSeats(ConcertSchedule concertSchedule) {
    return concertSeatJPARepository.countAllAvailableConcertSeats(concertSchedule);
  }

  @Override
  public Optional<ConcertSeat> findByIdForUpdate(Long concertSeatId) {
    return concertSeatJPARepository.findByIdForUpdate(concertSeatId);
  }

  @Override
  public Optional<ConcertSeat> findById(Long concertSeatId) {
    return concertSeatJPARepository.findById(concertSeatId);
  }
}
