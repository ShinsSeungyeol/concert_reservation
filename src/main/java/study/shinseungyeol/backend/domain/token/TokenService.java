package study.shinseungyeol.backend.domain.token;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

  public static final int NUM_OF_ACTIVE_TOKEN_PER_INTERVAL = 50;
  private final TokenRepository tokenRepository;

  /**
   * 토큰이 존재한다면 대기열 상태로 변환하고, 존재하지 않는다면 토큰을 생성한다.
   *
   * @param memberId
   * @return
   */
  public UUID createOrUpdateStatusToPending(Long memberId) {
    return tokenRepository.findByMemberIdForUpdate(memberId)
        .map(existToken -> {
          existToken.toPending();
          return existToken.getId();
        })
        .orElseGet(
            () -> {
              Token token = Token.create(memberId);
              tokenRepository.save(token);
              return token.getId();
            }
        );
  }

  /**
   * 5분 마다 실행하여 오래 대기한 NUM_OF_ACTIVE_TOKEN_PER_INTERVAL의 사용자를 ACTIVE 상태로 만드는 함수
   */
  @Scheduled(cron = "0 0/5 * * * *")
  public void activatePendingTokensPerInterval() {
    System.out.println("NUM_OF_ACTIVE_TOKEN_PER_INTERVAL = " + NUM_OF_ACTIVE_TOKEN_PER_INTERVAL);

    Pageable pageable = PageRequest.of(0, NUM_OF_ACTIVE_TOKEN_PER_INTERVAL);
    tokenRepository.findAllByStatusOrderByUpdateAtAsc(TokenStatus.PENDING, pageable).stream()
        .forEach(
            token -> token.toActive()
        );
  }

  /**
   * 토큰을 조회하고 active 상태인것을 테스트한다
   *
   * @param uuid
   * @return
   */
  public Token getTokenWithValidateActive(UUID uuid) {
    Token token = tokenRepository.findByIdForUpdate(uuid)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));
    token.validateActive();

    return token;
  }

  /**
   * 토큰을 조회한다.
   *
   * @param uuid
   * @return
   */
  public Token getToken(UUID uuid) {
    return tokenRepository.findByIdForUpdate(uuid)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));

  }

  /**
   * 토큰을 사용하지 못하도록 변경
   *
   * @param uuid
   */
  public void convertToInactiveToken(UUID uuid) {
    tokenRepository.findByIdForUpdate(uuid)
        .ifPresent(token -> token.toInactive());

  }
}
