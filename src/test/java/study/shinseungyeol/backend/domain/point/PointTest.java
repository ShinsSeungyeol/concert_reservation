package study.shinseungyeol.backend.domain.point;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.shinseungyeol.backend.domain.member.Member;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

class PointTest {

  private Member member;
  private Point point;

  @BeforeEach
  public void setUp() {
    point = new Point(1L, 1L, BigDecimal.valueOf(100));
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 memberId가 Null 인 경우 생성이 안되어야 함")
  public void 정적팩토리_member_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        Point.create(null));
  }

  @Test
  @DisplayName("addPoint 정상 동작 테스트")
  public void addPoint_정상인_경우() {
    BigDecimal balance = point.getBalanceAmount();
    BigDecimal balanceToAdd = BigDecimal.valueOf(100);

    point.addPoint(balanceToAdd);

    Assertions.assertEquals(balance.add(balanceToAdd), point.getBalanceAmount());

  }

  @Test
  @DisplayName("addPoint 메서드는 더하려는 amount가 음수인 경우, IllegalArgumentException")
  public void addPoint_amount_음수인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> point.addPoint(BigDecimal.valueOf(-1L)));
  }

  @Test
  @DisplayName("usePoint 정상동작 테스트")
  public void usePoint_정상인_경우() {
    BigDecimal balance = point.getBalanceAmount();
    BigDecimal balanceToUse = BigDecimal.valueOf(100);

    point.usePoint(balanceToUse);

    Assertions.assertEquals(balance.subtract(balanceToUse), point.getBalanceAmount());
  }

  @Test
  @DisplayName("usePoint 메서드는 사용하려는 amount가 음수인 경우, IllegalArgumentException")
  public void usePoint_amount_음수인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> point.usePoint(BigDecimal.valueOf(-1L)));
  }

  @Test
  @DisplayName("usePoint 메서드는 사용하려는 amount가 balance amount 보다 적은 경우, IllegalStateException")
  public void usePoint_amount가_balance보다_큰_경우() {
    BigDecimal balance = point.getBalanceAmount();
    BigDecimal balanceToUse = balance.add(BigDecimal.valueOf(1));

    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> point.usePoint(balanceToUse));

    Assertions.assertEquals(ErrorCode.NOT_ENOUGH_BALANCE, customException.getErrorCode());
  }
}