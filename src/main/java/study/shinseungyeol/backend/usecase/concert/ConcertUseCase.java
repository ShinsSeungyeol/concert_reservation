package study.shinseungyeol.backend.usecase.concert;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.shinseungyeol.backend.domain.concert.ConcertService;
import study.shinseungyeol.backend.domain.token.TokenService;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSchedule;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSeat;

@Component
@RequiredArgsConstructor
public class ConcertUseCase {

  private final ConcertService concertService;
  private final TokenService tokenService;

  public List<AvailableConcertSchedule.QueryResult> getAvailableConcertSchedules(
      AvailableConcertSchedule.Query query) {
    tokenService.getTokenWithValidateActive(query.getUuid());

    return concertService.getAvailableConcertSchedules(
        query.getConcertID()).stream().map(AvailableConcertSchedule.QueryResult::of).toList();
  }

  public List<AvailableConcertSeat.QueryResult> getAvailableConcertSeats(
      AvailableConcertSeat.Query query) {
    tokenService.getTokenWithValidateActive(query.getUuid());

    return concertService.getAvailableConcertSeats(query.getConcertId()).stream()
        .map(AvailableConcertSeat.QueryResult::of).toList();
  }

}
