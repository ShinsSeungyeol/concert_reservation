package study.shinseungyeol.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.shinseungyeol.backend.api.token.dto.CreateTokenDto;
import study.shinseungyeol.backend.api.token.dto.CreateTokenDto.ResponseCreateToken;
import study.shinseungyeol.backend.api.token.dto.TokenStatusDto;
import study.shinseungyeol.backend.api.token.dto.TokenStatusDto.ResponseTokenStatus;
import study.shinseungyeol.backend.domain.token.TokenService;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

  private final TokenService tokenService;

  @PostMapping
  @Operation(summary = "토큰 발급 API", description = "토큰을 발급하거나 상태를 업데이트합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "토큰 발급 성공", content = {
          @Content(schema = @Schema(implementation = ResponseCreateToken.class))}),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 ID 입니다.", content = {
          @Content(schema = @Schema(implementation = ErrorResponse.class))})
  })
  public ResponseEntity<CreateTokenDto.ResponseCreateToken> createOrUpdateToken(
      @RequestBody CreateTokenDto.RequestCreateToken request) {

    UUID uuid = tokenService.createOrUpdateStatusToPending(request.memberId());
    CreateTokenDto.ResponseCreateToken response = new CreateTokenDto.ResponseCreateToken(uuid);

    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  @Operation(summary = "토큰 상태 조회 API", description = "토큰 상태를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "토큰 상태 조회 성공", content = {
          @Content(schema = @Schema(implementation = ResponseTokenStatus.class))}),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 토큰입니다.", content = {
          @Content(schema = @Schema(implementation = ErrorResponse.class))})
  })
  public ResponseEntity<TokenStatusDto.ResponseTokenStatus> getTokenStatus(
      @RequestParam UUID token) {

    TokenStatusDto.ResponseTokenStatus response = TokenStatusDto.ResponseTokenStatus.of(
        tokenService.getToken(token)
    );

    return ResponseEntity.ok().body(response);
  }
}