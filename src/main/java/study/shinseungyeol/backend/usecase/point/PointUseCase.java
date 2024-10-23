package study.shinseungyeol.backend.usecase.point;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.point.Point;
import study.shinseungyeol.backend.domain.point.PointService;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenService;
import study.shinseungyeol.backend.usecase.point.dto.ChargePoint;
import study.shinseungyeol.backend.usecase.point.dto.GetPoint;
import study.shinseungyeol.backend.usecase.point.dto.UsePoint;

@Component
@RequiredArgsConstructor
public class PointUseCase {

  private final PointService pointService;
  private final TokenService tokenService;
  private final ConcertSeatReservationService concertSeatReservationService;
  private final ConcertService concertService;

  /**
   * 포인트 사용하는 유즈케이스
   *
   * @param command
   * @return
   */
  public UsePoint.CommandResult usePointWithValidateToken(UsePoint.Command command) {
    Token token = tokenService.getTokenWithValidateActive(command.getUuid());

    ConcertSeatReservation concertSeatReservation =
        concertSeatReservationService.completeConcertSeatReservation(command.getReservationId());

    ConcertSeat concertSeat = concertService.getConcertSeat(
        concertSeatReservation.getConcertSeatId());

    Point point = pointService.usePoint(token.getMemberId(), concertSeat.getPrice());

    return UsePoint.CommandResult.of(point);
  }


  /**
   * @param command
   * @return
   */
  public ChargePoint.CommandResult chargePointWithValidateToken(ChargePoint.Command command) {
    Token token = tokenService.getTokenWithValidateActive(command.getUuid());

    return ChargePoint.CommandResult.of(
        pointService.chargePoint(token.getMemberId(), command.getChargingAmount()));
  }


  /**
   * 토큰이 액티브 상태라면 포인트를 조회한다.
   *
   * @param uuid
   */
  public GetPoint.QueryResult getPointAmountWithValidateToken(UUID uuid) {
    Token token = tokenService.getTokenWithValidateActive(uuid);

    return GetPoint.QueryResult.of(pointService.getPointByMemberId(token.getMemberId()));

  }
}
