package study.shinseungyeol.backend.domain.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Token extends BaseEntity {

  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private Long memberId;

  @Enumerated(EnumType.STRING)
  private TokenStatus status;

  /**
   * 토큰 정적 팩토리 메서드
   *
   * @param memberId
   * @return
   */
  public static Token create(Long memberId) {
    if (memberId == null) {
      throw new IllegalArgumentException("memberId cannot be null");
    }

    Token token = new Token();
    token.setMemberId(memberId);
    token.setStatus(TokenStatus.PENDING);
    token.setId(UUID.randomUUID());

    return token;
  }

  /**
   * 토큰이 사용 가능한 상태인지를 체크한다.
   */
  public void validateActive() {
    if (this.getStatus() != TokenStatus.ACTIVE) {
      throw new IllegalStateException("Token is not ACTIVE");
    }
  }

  /**
   * 대기 상태로 변환
   */
  public void toPending() {
    setStatus(TokenStatus.PENDING);
  }

  /**
   * 액티브 상태로 변환
   */
  public void toActive() {
    setStatus(TokenStatus.ACTIVE);
  }

  /**
   * 인액티브 상태로 변환
   */
  public void toInactive() {
    setStatus(TokenStatus.INACTIVE);
  }
}
