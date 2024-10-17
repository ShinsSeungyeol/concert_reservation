package study.shinseungyeol.backend.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertDatesDto.AvailableConcertDate;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertDatesDto.RequestAvailableConcertDates;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertDatesDto.ResponseAvailableConcertDates;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertSeatsDto.RequestAvailableConcertSeats;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertSeatsDto.ResponseAvailableConcertSeats;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.usecase.concert.ConcertUseCase;

@RestController
@RequestMapping("/api/v1/concert")
@RequiredArgsConstructor
public class ConcertController {

  private final ConcertUseCase concertUseCase;

  @GetMapping("/available-dates")
  @Operation(summary = "콘서트 예약 가능 날짜들 조회", description = "현재 기준으로 예약 가능한 콘서트와 날짜 정보를 반환한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "날짜 목록 조회 성공", content = {
          @Content(schema = @Schema(implementation = ResponseAvailableConcertDates.class))
      }),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<ResponseAvailableConcertDates> getAvailableConcertDates(
      RequestAvailableConcertDates request) {

    List<ConcertSchedule> concertSchedules = concertUseCase.getAvailableConcertSchedules(
        request.token(),
        request.concertId());

    return ResponseEntity.ok().body(new ResponseAvailableConcertDates(concertSchedules.stream().map(
        concertSchedule -> new AvailableConcertDate(concertSchedule.getStartAt(),
            concertSchedule.getEndAt(),
            request.concertId())).toList()));
  }

  @GetMapping("/available-seats")
  @Operation(summary = "콘서트 예약 가능 좌석 반환", description = "콘서트 예약 가능 좌석 정보들을 반환한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "현재 기준으로 콘서트 예약 가능 좌석 목록 조회 성공.", content = {
          @Content(schema = @Schema(implementation = ResponseAvailableConcertSeats.class))
      }),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<ResponseAvailableConcertSeats> getAvailableConcertSeats(
      RequestAvailableConcertSeats request) {
    List<ConcertSeat> concertSeats = concertUseCase.getAvailableConcertSeats(request.token(),
        request.concertId());

    return ResponseEntity.ok().body(new ResponseAvailableConcertSeats(
        request.concertId(),
        concertSeats.stream().map(concertSeat -> concertSeat.getId()).toList()));
  }

}
