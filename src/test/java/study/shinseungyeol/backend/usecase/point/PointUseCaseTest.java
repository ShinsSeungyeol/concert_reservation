package study.shinseungyeol.backend.usecase.point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertRepository;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertScheduleRepository;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertSeatRepository;
import study.shinseungyeol.backend.domain.member.Member;
import study.shinseungyeol.backend.domain.point.Point;
import study.shinseungyeol.backend.domain.point.PointHistoryRepository;
import study.shinseungyeol.backend.domain.point.PointRepository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationRepository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.usecase.point.dto.ChargePoint;
import study.shinseungyeol.backend.usecase.point.dto.UsePoint;
import study.shinseungyeol.backend.usecase.point.dto.UsePoint.Command;

@SpringBootTest
class PointUseCaseTest {

  @Autowired
  private PointUseCase pointUseCase;
  @Autowired
  private TokenRepository tokenRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ConcertRepository concertRepository;
  @Autowired
  private ConcertScheduleRepository concertScheduleRepository;
  @Autowired
  private ConcertSeatRepository concertSeatRepository;
  @Autowired
  private PointRepository pointRepository;
  @Autowired
  private ConcertSeatReservationService concertSeatReservationService;
  @Autowired
  private ConcertSeatReservationRepository concertSeatReservationRepository;
  @Autowired
  private PointHistoryRepository pointHistoryRepository;


  private Member member;
  private Token token;
  private Point point;
  private Concert concert;
  private ConcertSchedule concertSchedule;
  private ConcertSeat concertSeat;
  private Long reservationId;



  @AfterEach
  public void tearDown() throws Exception {
    tokenRepository.deleteAll();
    memberRepository.deleteAll();
    pointHistoryRepository.deleteAll();
    pointRepository.deleteAll();
    concertSeatReservationRepository.deleteAll();
    concertSeatRepository.deleteAll();
    concertScheduleRepository.deleteAll();
    concertRepository.deleteAll();
  }

  @BeforeEach
  public void setUp() {
    member = memberRepository.save(Member.create("신승열"));
    token = tokenRepository.save(new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));
    point = pointRepository.save(new Point(null, member.getId(), BigDecimal.valueOf(1000)));
    concert = concertRepository.save(Concert.create("TESTCONCERT"));
    concertSchedule = concertScheduleRepository.save(
        ConcertSchedule.create(concert, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
    concertSeat = concertSeatRepository.save(
        ConcertSeat.create(concertSchedule, 1, BigDecimal.TEN));
    reservationId = concertSeatReservationService.createConcertSeatReservation(member.getId(),
        concertSeat.getId()).getId();
  }

  @Test
  public void 포인트_사용_예약_내역_없는_경우_에러() {
    Command command = new Command(token.getId(), 300L);

    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> pointUseCase.usePoint(command));

    Assertions.assertEquals(ErrorCode.NOT_FOUND_RESERVATION, customException.getErrorCode());
  }

  @Test
  public void 포인트_사용_정상_동작_테스트() {
    Command command = new Command(token.getId(), reservationId);

    BigDecimal initBalance = point.getBalanceAmount();

    BigDecimal actual = pointUseCase.usePoint(command).getBalance();

    Assertions.assertEquals(0, initBalance.subtract(concertSeat.getPrice()).compareTo(actual));

    List<ConcertSeatReservation> reservations = concertSeatReservationRepository.findAll()
        .stream()
        .filter(reservation -> reservation.getMemberId().equals(member.getId()))
        .toList();

    Assertions.assertEquals(reservations.get(0).getReservationStatus(),
        ReservationStatus.COMPLETED);
  }

  @Test
  public void 포인트충전_정상_동작() {
    BigDecimal init = point.getBalanceAmount();
    BigDecimal charge = BigDecimal.TEN;

    ChargePoint.Command command = new ChargePoint.Command(token.getId(), charge);

    BigDecimal actual = pointUseCase.chargePoint(command).getBalance();
    Assertions.assertEquals(0, init.add(charge).compareTo(actual));
  }


  @Test
  public void 포인트조회_정상_동작() {
    BigDecimal expected = point.getBalanceAmount();

    BigDecimal actual = pointUseCase.getPointAmount(token.getId())
        .getBalance();

    Assertions.assertEquals(0, actual.compareTo(expected));
  }

  @Test
  @DisplayName("한 사용자가 동시에 포인트를 충전할 경우, 수정 이상이 발생하면 안된다.")
  public void 포인트_충전_동시성_테스트() throws InterruptedException {
    final int TRY_COUNT = 10;
    final int amountToCharge = 100;
    BigDecimal initBalance = point.getBalanceAmount();

    ExecutorService executorService = Executors.newFixedThreadPool(TRY_COUNT);
    CountDownLatch latch = new CountDownLatch(TRY_COUNT);

    for (int i = 0; i < TRY_COUNT; i++) {
      executorService.submit(() -> {
        try {
          pointUseCase.chargePoint(
              new ChargePoint.Command(token.getId(), BigDecimal.valueOf(amountToCharge)));
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    BigDecimal expected = initBalance.add(BigDecimal.valueOf(amountToCharge)
        .multiply(BigDecimal.valueOf(TRY_COUNT)));

    BigDecimal actual = pointUseCase.getPointAmount(token.getId()).getBalance();

    Assertions.assertEquals(0, actual.compareTo(expected));
  }

  @Test
  @DisplayName("한 사용자가 동시에 포인트를 사용할 경우, 최초 한번만 결제 된다.")
  public void 포인트_결제_한번만_성공_동시성_테스트() throws InterruptedException {
    final int TRY_COUNT = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(TRY_COUNT);
    AtomicInteger successCount = new AtomicInteger(0);

    for (int i = 0; i < TRY_COUNT; i++) {
      executorService.submit(() -> {
        try {
          pointUseCase.usePoint(
              new UsePoint.Command(token.getId(), reservationId));
          successCount.incrementAndGet();
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    Assertions.assertEquals(1, successCount.get());

  }
}