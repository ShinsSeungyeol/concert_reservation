package study.shinseungyeol.backend.api.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.reservation.dto.ReserveConcertSeatAPI;
import study.shinseungyeol.backend.api.reservation.dto.ReserveConcertSeatAPI.Response;
import study.shinseungyeol.backend.usecase.reservation.ContentSeatReservationUseCase;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

  private final ContentSeatReservationUseCase contentSeatReservationUseCase;

  @PostMapping("/concert-seat")
  @Operation(summary = "콘서트 예약 API", description = "콘서트 좌석을 예약하는 API입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "콘서트 좌석을 예약 성공", content =
          {@Content(schema = @Schema(implementation = ReserveConcertSeatAPI.Request.class))}),
      @ApiResponse(responseCode = "404", description = "좌석이 존재하지 않습니다."),
      @ApiResponse(responseCode = "400", description = "좌석이 예약 가능한 상태가 아닙니다."),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다")
  })

  public ResponseEntity<ReserveConcertSeatAPI.Response> reserveConcertSeat(
      @RequestBody ReserveConcertSeatAPI.Request request) {
    Response response = Response.of(
        contentSeatReservationUseCase.reserveConcert(request.toCommand()));

    return ResponseEntity.ok()
        .body(response);
  }
}
