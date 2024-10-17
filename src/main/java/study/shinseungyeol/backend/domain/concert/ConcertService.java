package study.shinseungyeol.backend.domain.concert;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.infra.concert.ConcertRepository;
import study.shinseungyeol.backend.infra.concert.ConcertScheduleRepository;
import study.shinseungyeol.backend.infra.concert.ConcertSeatRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertService {

  private final ConcertRepository concertRepository;
  private final ConcertScheduleRepository concertScheduleRepository;
  private final ConcertSeatRepository concertSeatRepository;

  /**
   * 특정 콘서트의 예약 가능 좌석 조회
   *
   * @return
   */
  public List<ConcertSeat> getAvailableConcertSeats(Long id) {
    Concert concert = concertRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException());

    return concertSeatRepository.findAllByConcertSchedule_ConcertAndAvailableIsTrue(concert);
  }

  /**
   * 콘서트의 예약 가능 일자 조회
   *
   * @param id
   * @return
   */
  public List<ConcertSchedule> getAvailableConcertSchedules(Long id) {
    Concert concert = concertRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException());

    return concertScheduleRepository.findAllByConcert(
            concert).stream()
        .filter(concertSchedule ->
            concertSeatRepository.countAllByConcertScheduleAndAvailableIsTrue(concertSchedule) > 0)
        .toList();
  }


  /**
   * 예약 가능한 좌석을 조회하는 함수
   *
   * @param concertSeatId
   * @return
   */
  public void checkAvailableConcertSeatWithLock(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new NoSuchElementException());

    if (!concertSeat.getAvailable()) {
      throw new IllegalStateException("not available");
    }
  }
}
