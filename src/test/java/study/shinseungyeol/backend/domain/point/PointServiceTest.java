package study.shinseungyeol.backend.domain.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.shinseungyeol.backend.infra.point.PointHistoryRepository;
import study.shinseungyeol.backend.infra.point.PointRepository;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

  @Mock
  PointHistoryRepository pointHistoryRepository;
  @InjectMocks
  private PointService pointService;
  @Mock
  private PointRepository pointRepository;

  private Point point;

  @BeforeEach
  public void setUp() {
    Long memberId = 1L;
    BigDecimal balance = BigDecimal.valueOf(100);
    point = new Point(1L, memberId, balance);
  }

  @Test
  @DisplayName("포인트 사용 정상 동작 테스트")
  public void 포인트_사용_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal balance = point.getBalanceAmount();

    BigDecimal amountToUse = BigDecimal.valueOf(100);

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    pointService.usePoint(memberId, amountToUse);

    verify(pointHistoryRepository).save(any(PointHistory.class));
    assertEquals(balance.subtract(amountToUse), point.getBalanceAmount());
  }

  @Test
  @DisplayName("포인트 사용시, 잔고가 적은 경우 IllegalStateException")
  public void 포인트_사용_잔고_부족_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal amountToUse = point.getBalanceAmount().add(BigDecimal.valueOf(1));

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    assertThrows(IllegalStateException.class, () -> pointService.usePoint(memberId, amountToUse));
  }

  @Test
  @DisplayName("포인트 사용시, 사용 포인트가 음수인 경우 IllegalArgumentException")
  public void 포인트_사용_음수_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal amountToUse = BigDecimal.valueOf(-1);

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    assertThrows(IllegalArgumentException.class,
        () -> pointService.usePoint(memberId, amountToUse));
  }


  @Test
  @DisplayName("포인트 충전 정상 동작 테스트")
  public void 포인트_충전_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal balance = point.getBalanceAmount();
    BigDecimal amountToCharge = BigDecimal.valueOf(100);

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    pointService.chargePoint(memberId, amountToCharge);

    verify(pointHistoryRepository).save(any(PointHistory.class));

    assertEquals(balance.add(amountToCharge), point.getBalanceAmount());
  }

  @Test
  @DisplayName("포인트 충전시, 충전 포인트가 음수인 경우 IllegalArgumentException")
  public void 포인트_충전_음수_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal amountToCharge = BigDecimal.valueOf(-1);

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    assertThrows(IllegalArgumentException.class,
        () -> pointService.chargePoint(memberId, amountToCharge));
  }
}