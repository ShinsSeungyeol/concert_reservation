package study.shinseungyeol.backend.api.token;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.token.dto.CreateTokenDto.RequestCreateToken;
import study.shinseungyeol.backend.api.token.dto.CreateTokenDto.ResponseCreateToken;
import study.shinseungyeol.backend.api.token.dto.TokenStatusDto.RequestTokenStatus;
import study.shinseungyeol.backend.api.token.dto.TokenStatusDto.ResponseTokenStatus;


@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

  @PostMapping
  @Operation(summary = "토큰 발급 API", description = "토큰을 발급한다. 이전 토큰 발급이 되어있는 경우에는 이전 토큰을 반환한다. 이전 토큰이 만약 만료된 경우에는 이전 토큰을 다시 대기열에 넣는다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "토큰 발급 성공", content = {
          @Content(schema = @Schema(implementation = ResponseCreateToken.class))}),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 ID 입니다.")
  })
  public ResponseEntity<ResponseCreateToken> createOrChangeActiveStatus(
      @RequestBody RequestCreateToken requestDto) {
    return ResponseEntity.ok().body(new ResponseCreateToken(UUID.randomUUID()));
  }

  @GetMapping
  @Operation(summary = "토큰 상태 조회 API", description = "토큰 상태를 조회한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "토큰 상태 조회 성공", content = {
          @Content(schema = @Schema(implementation = ResponseTokenStatus.class))
      }),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 토큰입니다.")
  })
  public ResponseEntity<ResponseTokenStatus> getStatus(RequestTokenStatus request) {
    return ResponseEntity.ok().body(new ResponseTokenStatus(request.token(), "ACTIVE"));
  }

}
