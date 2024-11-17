package study.shinseungyeol.backend.domain.concert;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CONCERT));

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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CONCERT));

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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));
  }

  /**
   * 콘서트 좌석 점유 처리
   *
   * @param concertSeatId
   */
  public void convertConcertSeatToOccupied(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

    concertSeat.occupied();
  }


  /**
   * 레디스를 분산락을 이용한 좌석 점유 처리
   *
   * @param concertSeatId
   */
  public void convertToConcertSeatToOccupiedUsingRedis(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findById(concertSeatId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

    concertSeat.occupied();
  }

  /**
   * 콘서트 좌석 사용 가능 처리
   *
   * @param concertSeatId
   */
  public void convertConcertSeatToAvailable(Long concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findByIdForUpdate(concertSeatId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

    concertSeat.available();
  }


}
