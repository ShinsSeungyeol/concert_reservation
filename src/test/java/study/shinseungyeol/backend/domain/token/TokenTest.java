package study.shinseungyeol.backend.domain.token;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenTest {

  private Token pendingToken;
  private Token activeToken;
  private Token inactiveToken;


  @BeforeEach
  public void setUp() throws Exception {
    pendingToken = new Token(UUID.randomUUID(), 1L, TokenStatus.PENDING);
    activeToken = new Token(UUID.randomUUID(), 2L, TokenStatus.ACTIVE);
    inactiveToken = new Token(UUID.randomUUID(), 3L, TokenStatus.INACTIVE);
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 memberId가 null인 경우 생성이 안되어야 함")
  public void 정적팩토리_member_id가_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Token.create(null);
    });
  }

  @Test
  @DisplayName("액티브 상태가 아닌 토큰이 에러가 나는지 체크")
  public void validateActive_테스트() {
    Assertions.assertThrows(IllegalStateException.class, () -> {
      inactiveToken.validateActive();
    });

    Assertions.assertThrows(IllegalStateException.class, () -> {
      pendingToken.validateActive();
    });
  }
  

  @Test
  @DisplayName("대기 상태로 변환 테스트")
  public void toPending_정상동작_테스트() {
    pendingToken.toPending();
    activeToken.toPending();
    inactiveToken.toPending();

    Assertions.assertEquals(TokenStatus.PENDING, pendingToken.getStatus());
    Assertions.assertEquals(TokenStatus.PENDING, activeToken.getStatus());
    Assertions.assertEquals(TokenStatus.PENDING, inactiveToken.getStatus());
  }

  @Test
  @DisplayName("액티브 상태로 변환 테스트")
  public void toActive() {
    pendingToken.toActive();
    activeToken.toActive();
    inactiveToken.toActive();

    Assertions.assertEquals(TokenStatus.ACTIVE, pendingToken.getStatus());
    Assertions.assertEquals(TokenStatus.ACTIVE, activeToken.getStatus());
    Assertions.assertEquals(TokenStatus.ACTIVE, inactiveToken.getStatus());
  }

  @Test
  @DisplayName("인액티브 상태로 변환 테스트")
  public void toInactive() {
    pendingToken.toInactive();
    activeToken.toInactive();
    inactiveToken.toInactive();

    Assertions.assertEquals(TokenStatus.INACTIVE, pendingToken.getStatus());
    Assertions.assertEquals(TokenStatus.INACTIVE, activeToken.getStatus());
    Assertions.assertEquals(TokenStatus.INACTIVE, inactiveToken.getStatus());
  }
}