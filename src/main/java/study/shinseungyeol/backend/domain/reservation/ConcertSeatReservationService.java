package study.shinseungyeol.backend.domain.reservation;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertSeatReservationService {

  private final ConcertSeatReservationRepository reservationRepository;

  /**
   * 콘서트 좌석 예약
   *
   * @param memberId
   * @param concertSeatId
   */
  public ConcertSeatReservation createConcertSeatReservation(Long memberId, Long concertSeatId) {
    ConcertSeatReservation concertSeatReservation = ConcertSeatReservation.create(memberId,
        concertSeatId);
    reservationRepository.save(concertSeatReservation);

    return concertSeatReservation;
  }



  /**
   * 예약을 취소하고 취소한 좌석 Id를 반환
   *
   * @return
   */
  public List<Long> cancelPendingReservationAndGetSeatIdsPerInterval() {
    return reservationRepository.findAllExpired(ReservationStatus.PENDING, LocalDateTime.now())
        .stream()
        .map(concertSeatReservation -> {
          concertSeatReservation.cancel();
          return concertSeatReservation.getConcertSeatId();
        })
        .toList();
  }


  /**
   * 좌석 예약을 완료 시킴(결제 후)
   *
   * @param concertSeatReservationId
   */
  public ConcertSeatReservation completeConcertSeatReservation(Long concertSeatReservationId) {
    ConcertSeatReservation concertSeatReservation = reservationRepository.findByIdForUpdate(
            concertSeatReservationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));

    concertSeatReservation.checkIsExpired();

    concertSeatReservation.complete();

    return concertSeatReservation;
  }

}
