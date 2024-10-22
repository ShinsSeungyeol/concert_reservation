package study.shinseungyeol.backend.domain.concert;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
  public List<ConcertSeat> getAvailableConcertSeats(Long concertId) {
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new NoSuchElementException());

    return concertSeatRepository.findAllAvailableSeats(concert);
  }

  /**
   * 콘서트의 예약 가능 일자 조회
   *
   * @param concertId
   * @return
   */
  public List<ConcertSchedule> getAvailableConcertSchedules(Long concertId) {
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new NoSuchElementException());

    return concertScheduleRepository.findAllByConcert(
            concert).stream()
        .filter(concertSchedule ->
            concertSeatRepository.countAllAvailableSeats(concertSchedule) > 0)
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

  /**
   * 콘서트 좌석 조회
   *
   * @param concertSeatId
   * @return
   */
  public ConcertSeat getConcertSeat(Long concertSeatId) {
    return concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new NoSuchElementException());
  }

  /**
   * 콘서트 좌석 점유 처리
   *
   * @param concertSeatId
   */
  public void convertConcertSeatToOccupied(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new NoSuchElementException());

    concertSeat.occupied();
  }

  /**
   * 콘서트 좌석 사용 가능 처리
   *
   * @param concertSeatId
   */
  public void convertConcertSeatToAvailable(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new NoSuchElementException());

    concertSeat.available();
  }


}
