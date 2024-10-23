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
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertScheduleAPI;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertSeatAPI;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertSeatAPI.Request;
import study.shinseungyeol.backend.api.concert.dto.AvailableConcertSeatAPI.Response;
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
          @Content(schema = @Schema(implementation = AvailableConcertScheduleAPI.Response.class))
      }),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<List<AvailableConcertScheduleAPI.Response>> getAvailableConcertDates(
      AvailableConcertScheduleAPI.Request request) {

    List<AvailableConcertScheduleAPI.Response> responses = concertUseCase.getAvailableConcertSchedules(
        request.toQuery()).stream().map(AvailableConcertScheduleAPI.Response::of).toList();

    return ResponseEntity.ok().body(responses);
  }

  @GetMapping("/available-seats")
  @Operation(summary = "콘서트 예약 가능 좌석 반환", description = "콘서트 예약 가능 좌석 정보들을 반환한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "현재 기준으로 콘서트 예약 가능 좌석 목록 조회 성공.", content = {
          @Content(schema = @Schema(implementation = Response.class))
      }),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<List<AvailableConcertSeatAPI.Response>> getAvailableConcertSeats(
      Request request) {
    List<Response> responses = concertUseCase.getAvailableConcertSeats(request.toQuery()).stream()
        .map(Response::of).toList();

    return ResponseEntity.ok().body(responses);
  }

}
