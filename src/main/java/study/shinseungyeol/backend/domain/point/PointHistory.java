package study.shinseungyeol.backend.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Point point;

  @Enumerated(EnumType.STRING)
  private PointHistoryType type;

  private BigDecimal amount;

  /**
   * PointHistory 정적 팩토리 메서드
   *
   * @param point
   * @param type
   * @param amount
   * @return
   */
  public static PointHistory create(Point point, PointHistoryType type, BigDecimal amount) {
    PointHistory history = new PointHistory();
    history.setPoint(point);
    history.setType(type);
    history.setAmount(amount);

    return history;
  }
}
