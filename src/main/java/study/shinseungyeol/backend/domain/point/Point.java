package study.shinseungyeol.backend.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;
import study.shinseungyeol.backend.domain.member.Member;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private Member member;

  private BigDecimal amount = BigDecimal.ZERO;

  /**
   * 포인트 정적 팩토리 함수
   *
   * @param member
   * @return
   */
  public static Point create(Member member) {
    Point point = new Point();
    point.member = member;

    return point;
  }
}
