package study.shinseungyeol.backend.domain.point;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceIntegrationTest {

  @Autowired
  private PointService pointService;

  @Autowired
  private PointRepository pointRepository;

  @Autowired
  private PointHistoryRepository pointHistoryRepository;

  @AfterEach
  public void cleanUp() {
    pointHistoryRepository.deleteAll();
    pointRepository.deleteAll();  // 포인트 데이터를 모두 삭제
  }


  @Test
  @DisplayName("한 사용자가 동시에 포인트를 충전할 경우, 수정 이상이 발생하면 안된다.")
  public void 포인트_충전_동시성_테스트() throws InterruptedException {
    Point point = pointRepository.save(new Point(1L, 1L, BigDecimal.ZERO));

    final int TRY_COUNT = 10;
    final int amountToCharge = 100;

    ExecutorService executorService = Executors.newFixedThreadPool(TRY_COUNT);
    CountDownLatch latch = new CountDownLatch(TRY_COUNT);

    for (int i = 0; i < TRY_COUNT; i++) {
      executorService.submit(() -> {
        try {
          pointService.chargePoint(point.getMemberId(), BigDecimal.valueOf(amountToCharge));
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    BigDecimal expected = BigDecimal.valueOf(amountToCharge)
        .multiply(BigDecimal.valueOf(TRY_COUNT));

    BigDecimal actual = pointService.getPointByMemberId(point.getMemberId());

    Assertions.assertEquals(0, actual.compareTo(expected));
  }

  @Test
  @DisplayName("한 사용자가 동시에 포인트를 사용하는 경우, 수정 이상이 발생하면 안된다.")
  public void 포인트_사용_동시성_테스트() throws InterruptedException {
    Point point = pointRepository.save(new Point(1L, 1L, BigDecimal.valueOf(1000)));

    final int TRY_COUNT = 10;
    final int amountToCharge = 100;

    ExecutorService executorService = Executors.newFixedThreadPool(TRY_COUNT);
    CountDownLatch latch = new CountDownLatch(TRY_COUNT);

    for (int i = 0; i < TRY_COUNT; i++) {
      executorService.submit(() -> {
        try {
          pointService.usePoint(point.getMemberId(), BigDecimal.valueOf(amountToCharge));
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    BigDecimal actual = pointService.getPointByMemberId(point.getMemberId());

    Assertions.assertEquals(0, actual.compareTo(BigDecimal.ZERO));
  }

  @Test
  @DisplayName("한 사용자가 동시에 충전과 사용을 요청하는 경우, 수정이상이 발생하면 안된다")
  public void 포인트_사용_충전_동시성_테스트() throws InterruptedException {
    final int CHARGE_COUNT = 100, USE_COUNT = 100;
    final int amountToCharge = 1000;
    final int amountToUse = 100;
    final int amount = amountToUse * USE_COUNT;

    Point point = pointRepository.save(new Point(1L, 1L, BigDecimal.valueOf(amount)));

    ExecutorService executorService = Executors.newFixedThreadPool(CHARGE_COUNT + USE_COUNT);

    CountDownLatch latch = new CountDownLatch(CHARGE_COUNT + USE_COUNT);

    for (int i = 0; i < USE_COUNT; i++) {
      executorService.submit(() -> {
        try {
          pointService.usePoint(point.getMemberId(), BigDecimal.valueOf(amountToUse));
        } finally {
          latch.countDown();
        }
      });
    }

    for (int i = 0; i < CHARGE_COUNT; i++) {
      try {
        pointService.chargePoint(point.getMemberId(), BigDecimal.valueOf(amountToCharge));
      } finally {
        latch.countDown();
      }
    }

    latch.await();
    executorService.shutdown();

    BigDecimal expected = BigDecimal.valueOf(amount)
        .add(BigDecimal.valueOf((amountToCharge * CHARGE_COUNT) - (amountToUse * USE_COUNT)));

    BigDecimal actual = pointService.getPointByMemberId(point.getMemberId());

    Assertions.assertEquals(0, actual.compareTo(expected));
  }
}
