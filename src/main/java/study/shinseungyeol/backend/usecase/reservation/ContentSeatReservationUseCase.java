package study.shinseungyeol.backend.usecase.reservation;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenService;

@Component
@RequiredArgsConstructor
@Transactional
public class ContentSeatReservationUseCase {

  private final TokenService tokenService;
  private final ConcertSeatReservationService concertSeatReservationService;
  private final ConcertService concertService;


  /**
   * 콘서트 좌석 예약 유즈 케이스
   *
   * @param uuid
   * @param concertSeatId
   * @return
   */
  public Long reserveConcert(UUID uuid, Long concertSeatId) {
    Token token = tokenService.getTokenWithValidateActive(uuid);

    concertService.convertConcertSeatToOccupied(concertSeatId);

    return concertSeatReservationService.createConcertSeatReservation(token.getMemberId(),
        concertSeatId);
  }
}
