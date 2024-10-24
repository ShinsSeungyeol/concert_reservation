package study.shinseungyeol.backend.domain.token;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @InjectMocks
  private TokenService tokenService;

  @Mock
  private TokenRepository tokenRepository;

  @Test
  @DisplayName("토큰 생성할 때, 이미 토큰이 있으면, 해당 토큰을 대기 상태로 변환 해야 한다.")
  public void 토큰_생성_이미_있는_경우() {
    Long memberId = 1L;
    Token token = new Token(UUID.randomUUID(), memberId, TokenStatus.INACTIVE);

    when(tokenRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(token));

    tokenService.createOrUpdateStatusToPending(memberId);

    Assertions.assertEquals(TokenStatus.PENDING, token.getStatus());
  }

  @Test
  @DisplayName("토큰 생성할 때, 토큰이 없다면 토큰을 생성 해야 한다.")
  public void 토큰_생성_토큰_없는_경우() {
    Long memberId = 1L;

    when(tokenRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.empty());

    tokenService.createOrUpdateStatusToPending(memberId);

    verify(tokenRepository).save(any(Token.class));
  }

  @Test
  @DisplayName("토큰이 존재하지 않는 경우, 토큰 조회시 에러가 나야 한다.")
  public void 토큰_없는_경우_토큰_조회_에러() {
    UUID uuid = java.util.UUID.randomUUID();
    when(tokenRepository.findByIdForUpdate(uuid)).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> tokenService.getToken(uuid));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_TOKEN, customException.getErrorCode());
  }

  @Test
  @DisplayName("getTokenWithValidateActive 호출 시에 토큰이 없다면 에러가 나야 한다.")
  public void 토큰_없는_경우_getTokenWithValidateActive() {
    UUID uuid = java.util.UUID.randomUUID();
    when(tokenRepository.findByIdForUpdate(uuid)).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> tokenService.getTokenWithValidateActive(uuid));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_TOKEN, customException.getErrorCode());
  }

  @Test
  @DisplayName("getTokenWithValidateActive 호출 시에 토큰이 액티브 상태가 아니라면 에러가 나야 한다.")
  public void 토큰_인액티브_상태일때_getTokenWithValidateActive_테스트() {
    Token inactiveToken = new Token(UUID.randomUUID(), 1L, TokenStatus.INACTIVE);

    when(tokenRepository.findByIdForUpdate(inactiveToken.getId())).thenReturn(
        Optional.of(inactiveToken));

    Assertions.assertThrows(IllegalStateException.class,
        () -> tokenService.getTokenWithValidateActive(inactiveToken.getId()));
  }

  @Test
  @DisplayName("getTokenWithValidateActive 호출 시에 토큰이 액티브 상태가 아니라면 에러가 나야 한다.")
  public void 토큰_팬딩_상태일때_getTokenWithValidateActive_테스트() {
    Token pendingToken = new Token(UUID.randomUUID(), 1L, TokenStatus.PENDING);

    when(tokenRepository.findByIdForUpdate(pendingToken.getId())).thenReturn(
        Optional.of(pendingToken));

    Assertions.assertThrows(IllegalStateException.class,
        () -> tokenService.getTokenWithValidateActive(pendingToken.getId()));
  }

  @Test
  @DisplayName("대기한 사용자를 액티브로 만드는 스케줄 함수 단위 테스트")
  public void 대기_사용자_TO_ACTIVE_테스트() {
    List<Token> tokens = List.of(Token.create(1L), Token.create(2L), Token.create(3L));
    Pageable pageable = PageRequest.of(0, TokenService.NUM_OF_ACTIVE_TOKEN_PER_INTERVAL);

    when(tokenRepository.findAllByStatusOrderByUpdateAtAsc(TokenStatus.PENDING, pageable
    )).thenReturn(tokens);

    tokenService.activatePendingTokensPerInterval();

    tokens.stream().forEach(actual ->
        Assertions.assertEquals(TokenStatus.ACTIVE, actual.getStatus())
    );
  }
}