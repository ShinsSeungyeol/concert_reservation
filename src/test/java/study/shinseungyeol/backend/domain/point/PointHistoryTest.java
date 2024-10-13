package study.shinseungyeol.backend.domain.point;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointHistoryTest {

  private Point point;

  @BeforeEach
  public void setUp() {
    point = Point.create(1L);
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 Point가 null인 경우 생성이 안되어야 함")
  public void 정적팩토리_point_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        PointHistory.create(null, PointHistoryType.USE, BigDecimal.valueOf(100L)));
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 PointHistoryType이 null인 경우 생성이 안되어야 함")
  public void 정적팩토리_point_history_type_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        PointHistory.create(point, null, BigDecimal.valueOf(100L)));
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 amount가 null인 경우 생성이 안되어야 함")
  public void 정적팩토리_amount_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        PointHistory.create(point, PointHistoryType.USE, null));
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 amount가 음수인 경우 생성이 안되어야 함")
  public void 정적팩토리_amount_음수인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        PointHistory.create(point, PointHistoryType.USE, BigDecimal.valueOf(-1L)));
  }

}