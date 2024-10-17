package study.shinseungyeol.backend.domain.point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
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
public class Point extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long memberId;

  private BigDecimal balanceAmount = BigDecimal.ZERO;

  /**
   * Point 정적 팩토리 메서드
   *
   * @param memberId
   * @return
   */
  public static Point create(Long memberId) {
    if (memberId == null) {
      throw new IllegalArgumentException("memberId can not be null");
    }
    Point point = new Point();
    point.setMemberId(memberId);

    return point;
  }

  /**
   * amount 를 증가하는 함수
   *
   * @param amount
   */
  public void addPoint(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }

    BigDecimal added = balanceAmount.add(amount);
    setBalanceAmount(added);
  }


  /**
   * amount 를 감소하는 함수
   *
   * @param amount
   */
  public void usePoint(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }

    if (balanceAmount.compareTo(amount) < 0) {
      throw new IllegalStateException(
          "Insufficient balance. The balance amount must be greater than or equal to the required amount.");
    }

    BigDecimal subtracted = balanceAmount.subtract(amount);
    setBalanceAmount(subtracted);
  }
}
