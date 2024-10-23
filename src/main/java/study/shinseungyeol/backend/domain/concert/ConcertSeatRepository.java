package study.shinseungyeol.backend.domain.concert;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository {

  ConcertSeat save(ConcertSeat concertSeat);

  List<ConcertSeat> findAllAvailableSeats(Concert concert);

  Long countAllAvailableSeats(ConcertSchedule concertSchedule);

  Optional<ConcertSeat> findByIdForUpdate(Long concertSeatId);

  Optional<ConcertSeat> findById(Long concertSeat);

}
