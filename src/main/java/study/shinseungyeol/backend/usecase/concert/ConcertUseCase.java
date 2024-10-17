package study.shinseungyeol.backend.usecase.concert;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.token.TokenService;

@Component
@RequiredArgsConstructor
public class ConcertUseCase {

  private final ConcertService concertService;
  private final TokenService tokenService;

  public List<ConcertSchedule> getAvailableConcertSchedules(UUID token, Long concertId) {
    tokenService.getTokenWithValidateActive(token);
    return concertService.getAvailableConcertSchedules(concertId);
  }

  public List<ConcertSeat> getAvailableConcertSeats(UUID token, Long concertId) {
    tokenService.getTokenWithValidateActive(token);
    return concertService.getAvailableConcertSeats(concertId);
  }

}
