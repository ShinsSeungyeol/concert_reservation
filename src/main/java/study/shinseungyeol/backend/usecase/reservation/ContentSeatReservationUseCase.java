package study.shinseungyeol.backend.usecase.reservation;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.common.DistributedLock;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenService;
import study.shinseungyeol.backend.usecase.reservation.dto.ReserveConcertSeat;
import study.shinseungyeol.backend.usecase.reservation.dto.ReserveConcertSeat.CommandResult;

@Component
@RequiredArgsConstructor

public class ContentSeatReservationUseCase {

  private final TokenService tokenService;
  private final ConcertSeatReservationService concertSeatReservationService;
  private final ConcertService concertService;


  /**
   * @param command
   * @return
   */
  @DistributedLock(key = "#command.getConcertSeatId")
  public CommandResult reserveConcert(ReserveConcertSeat.Command command) {
    Token token = tokenService.getToken(command.getUuid());

    concertService.convertToConcertSeatToOccupiedUsingRedis(command.getConcertSeatId());
    tokenService.convertToInactiveToken(command.getUuid());

    ConcertSeatReservation concertSeatReservation = concertSeatReservationService.createConcertSeatReservation(
        token.getMemberId(),
        command.getConcertSeatId());

    return ReserveConcertSeat.CommandResult.of(concertSeatReservation);
  }

  /**
   * 일정 시간동안 완료되지 않은 예약 취소 스케줄링
   */
  @Scheduled(cron = "0 0/1 * * * *")
  @Transactional
  public void cancelReservation() {
    List<Long> contentSeatIds = concertSeatReservationService.cancelPendingReservationAndGetSeatIdsPerInterval();

    contentSeatIds.stream().forEach(seatId -> {
      concertService.convertConcertSeatToAvailable(seatId);
    });
  }
}
