package study.shinseungyeol.backend.domain.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

  @InjectMocks
  private PointService pointService;
  @Mock
  private PointRepository pointRepository;
  @Mock
  private PointHistoryRepository pointHistoryRepository;

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

    BigDecimal actual = pointService.usePoint(memberId, amountToUse).getBalanceAmount();

    verify(pointHistoryRepository).save(any(PointHistory.class));
    assertEquals(balance.subtract(amountToUse), actual);
  }

  @Test
  @DisplayName("포인트 사용시, 멤버 없는 경우 NOT_FOUND_MEMBER Exception")
  public void 포인트_사용_멤버_없는_경우_테스트() {
    when(pointRepository.findByMemberIdForUpdate(1L)).thenReturn(Optional.empty());

    CustomException customException = assertThrows(CustomException.class,
        () -> pointService.usePoint(1L, BigDecimal.valueOf(100)));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_POINT, customException.getErrorCode());
  }

  @Test
  @DisplayName("포인트 사용시, 잔고가 적은 경우 IllegalStateException")
  public void 포인트_사용_잔고_부족_테스트() {
    Long memberId = point.getMemberId();
    BigDecimal amountToUse = point.getBalanceAmount().add(BigDecimal.valueOf(1));

    when(pointRepository.findByMemberIdForUpdate(memberId)).thenReturn(Optional.of(point));

    CustomException customException = assertThrows(CustomException.class,
        () -> pointService.usePoint(memberId, amountToUse));

    Assertions.assertEquals(ErrorCode.NOT_ENOUGH_BALANCE, customException.getErrorCode());
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

    BigDecimal actual = pointService.chargePoint(memberId, amountToCharge).getBalanceAmount();

    verify(pointHistoryRepository).save(any(PointHistory.class));

    assertEquals(balance.add(amountToCharge), actual);
  }

  @Test
  @DisplayName("포인트 충전시, 멤버 없는 경우 NOT_FOUND_MEMBER Exception")
  public void 포인트_충전_멤버_없는_경우_테스트() {
    when(pointRepository.findByMemberIdForUpdate(1L)).thenReturn(Optional.empty());

    CustomException customException = assertThrows(CustomException.class,
        () -> pointService.chargePoint(1L, BigDecimal.valueOf(100)));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_POINT, customException.getErrorCode());
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

  @Test
  public void 포인트_조회시_멤버_ID가_없는_경우_익셉션() {
    when(pointRepository.findByMemberId(any(Long.class))).thenReturn(Optional.empty());

    CustomException customException = assertThrows(CustomException.class,
        () -> pointService.getPointByMemberId(1L));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_POINT, customException.getErrorCode());
  }
}