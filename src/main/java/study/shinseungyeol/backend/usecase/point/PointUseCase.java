package study.shinseungyeol.backend.usecase.point;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.point.PointService;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenService;

@Component
@RequiredArgsConstructor
public class PointUseCase {

  private final PointService pointService;
  private final TokenService tokenService;
  private final ConcertSeatReservationService concertSeatReservationService;
  private final ConcertService concertService;

  /**
   * 토큰이 액티브 상태라면 포인트 를 사용한다.
   *
   * @param uuid
   * @param reservationId
   */
  public BigDecimal usePointWithValidateToken(UUID uuid, Long reservationId) {
    Token token = tokenService.getTokenWithValidateActive(uuid);

    ConcertSeatReservation concertSeatReservation =
        concertSeatReservationService.completeConcertSeatReservation(reservationId);

    ConcertSeat concertSeat = concertService.getConcertSeat(
        concertSeatReservation.getConcertSeatId());

    return pointService.usePoint(token.getMemberId(), concertSeat.getPrice());
  }


  /**
   * 토큰이 액티브 상태라면 포인트를 충전한다.
   *
   * @param uuid
   * @param amountToUse
   */
  public BigDecimal chargePointWithValidateToken(UUID uuid, BigDecimal amountToUse) {
    Token token = tokenService.getTokenWithValidateActive(uuid);

    return pointService.chargePoint(token.getMemberId(), amountToUse);
  }


  /**
   * 토큰이 액티브 상태라면 포인트를 조회한다.
   *
   * @param uuid
   */
  public BigDecimal getPointAmountWithValidateToken(UUID uuid) {
    Token token = tokenService.getTokenWithValidateActive(uuid);

    return pointService.getPointByMemberId(token.getMemberId());

  }
}
