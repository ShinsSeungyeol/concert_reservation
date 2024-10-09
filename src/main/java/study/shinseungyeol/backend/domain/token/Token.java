package study.shinseungyeol.backend.domain.token;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;
import study.shinseungyeol.backend.domain.member.Member;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

  @Id
  private UUID uuid;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private Member member;

  @Enumerated(EnumType.STRING)
  private TokenStatus status = TokenStatus.PENDING;


  /**
   * 토큰 생성 정적 팩토리 메서드
   *
   * @param member
   * @return
   */
  public Token create(Member member) {
    Token token = new Token();
    token.setMember(member);
    token.setUuid(UUID.randomUUID());

    return token;
  }
}
