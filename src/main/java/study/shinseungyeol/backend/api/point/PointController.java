package study.shinseungyeol.backend.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.point.dto.ChargingPointDto.RequestChargingPoint;
import study.shinseungyeol.backend.api.point.dto.ChargingPointDto.ResponseChargingPoint;
import study.shinseungyeol.backend.api.point.dto.PointDto.RequestPoint;
import study.shinseungyeol.backend.api.point.dto.PointDto.ResponsePoint;
import study.shinseungyeol.backend.api.point.dto.UsePointDto.RequestUsePoint;
import study.shinseungyeol.backend.api.point.dto.UsePointDto.ResponseUsePoint;
import study.shinseungyeol.backend.usecase.point.PointUseCase;

@RestController
@RequestMapping("/api/v1/point")
public class PointController {

  private final PointUseCase pointUseCase;

  public PointController(PointUseCase pointUseCase) {
    this.pointUseCase = pointUseCase;
  }

  @GetMapping
  @Operation(summary = "포인트 조회 API", description = "사용자의 포인트를 조회하는 API 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자의 포인트 조회에 성공", content = {
          @Content(schema = @Schema(implementation = ResponsePoint.class))}),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<ResponsePoint> getPoint(RequestPoint pointRequest) {

    BigDecimal balance = pointUseCase.getPointAmountWithValidateToken(pointRequest.token());

    return ResponseEntity.ok(new ResponsePoint(balance));
  }


  @PatchMapping("/use")
  @Operation(summary = "포인트 사용 API", description = "사용자 포인트를 사용하는 API 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "포인트 사용 성공", content = {
          @Content(schema = @Schema(implementation = ResponseUsePoint.class))
      }),
      @ApiResponse(responseCode = "400", description = "사용하려는 포인트가 음수일 수 없습니다."),
      @ApiResponse(responseCode = "402", description = "잔액이 부족합니다."),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<ResponseUsePoint> usePoint(@RequestBody RequestUsePoint request) {

    BigDecimal balance = pointUseCase.usePointWithValidateToken(request.token(),
        request.reservationId());

    return ResponseEntity.ok(new ResponseUsePoint(request.reservationId(), balance));
  }

  @PatchMapping("/charging")
  @Operation(summary = "포인트 충전 API", description = "사용자 포인트를 충전하는 API 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자 포인트 충전 성공", content = {
          @Content(schema = @Schema(implementation = ResponseChargingPoint.class))
      }),
      @ApiResponse(responseCode = "400", description = "충전하려는 포인트가 음수일 수 없습니다."),
      @ApiResponse(responseCode = "403", description = "토큰이 액티브 상태가 아닙니다.")
  })
  public ResponseEntity<ResponseChargingPoint> chargingPoint(
      @RequestBody RequestChargingPoint request) {

    BigDecimal balance = pointUseCase.chargePointWithValidateToken(request.token(),
        request.chargingAmount());

    return ResponseEntity.ok(new ResponseChargingPoint(balance));
  }

}
