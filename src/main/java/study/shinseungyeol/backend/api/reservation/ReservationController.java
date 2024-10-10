package study.shinseungyeol.backend.api.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.reservation.dto.ConcertReservationDto;
import study.shinseungyeol.backend.api.reservation.dto.ConcertReservationDto.RequestConcertReservation;
import study.shinseungyeol.backend.api.reservation.dto.ConcertReservationDto.ResponseConcertReservation;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

  @PostMapping("/concert-seat")
  @Operation(summary = "콘서트 예약 API", description = "콘서트 좌석을 예약하는 API입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "콘서트 좌석을 예약 성공", content =
          {@Content(schema = @Schema(implementation = ConcertReservationDto.class))}),
      @ApiResponse(responseCode = "404", description = "좌석이 존재하지 않습니다."),
      @ApiResponse(responseCode = "400", description = "좌석이 예약 가능한 상태가 아닙니다."),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다")
  })
  public ResponseEntity<ResponseConcertReservation> reserveConcertSeat(
      @RequestBody RequestConcertReservation request) {
    return ResponseEntity.ok()
        .body(new ResponseConcertReservation(1L, 1L, BigDecimal.valueOf(300)));
  }

}
