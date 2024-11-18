package study.shinseungyeol.backend.usecase.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.usecase.reservation.dto.ReserveConcertSeat;

@SpringBootTest
class ContentSeatReservationUseCaseTest {

  @Autowired
  private ContentSeatReservationUseCase concertSeatReservationUseCase;
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
  private ConcertSeatReservationRepository concertSeatReservationRepository;
  @Autowired
  private PointRepository pointRepository;
  @Autowired
  private PointHistoryRepository pointHistoryRepository;

  private Member member;
  private Concert concert;
  private ConcertSchedule concertSchedule;
  private ConcertSeat concertSeat;


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
    concert = concertRepository.save(Concert.create("test concert"));
    concertSchedule = concertScheduleRepository.save(
        ConcertSchedule.create(concert, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

    concertSeat = concertSeatRepository.save(
        ConcertSeat.create(concertSchedule, 1, BigDecimal.TEN));
  }


  @Test
  public void 콘서트좌석_예약_정상_테스트() {
    Token token = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));

    ReserveConcertSeat.Command command = new ReserveConcertSeat.Command(token.getId(),
        concertSeat.getId());

    concertSeatReservationUseCase.reserveConcert(command);

    Token actualToken = tokenRepository.findById(token.getId()).orElse(null);

    ConcertSeat actual = concertSeatRepository.findById(concertSeat.getId()).orElse(null);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(false, actual.getAvailable());
    Assertions.assertEquals(TokenStatus.INACTIVE, actualToken.getStatus());

  }

  @Test
  public void 만료된_예약_취소_정상_테스트() {
    Member newMember = memberRepository.save(Member.create("test2"));

    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(null,
        newMember.getId(), concertSeat.getId(),
        ReservationStatus.PENDING, LocalDateTime.now().minusSeconds(20));

    concertSeatReservationRepository.save(concertSeatReservation);

    concertSeatReservationUseCase.cancelReservation();

    ConcertSeatReservation actualReservation = concertSeatReservationRepository.findById(
        concertSeatReservation.getId()).orElse(null);

    Assertions.assertEquals(ReservationStatus.CANCELED,
        actualReservation.getReservationStatus());

    ConcertSeat actualConcertSeat = concertSeatRepository.findById(concertSeat.getId())
        .orElse(null);

    Assertions.assertNotNull(actualConcertSeat);
    Assertions.assertEquals(true, actualConcertSeat.getAvailable());

  }

  @Test
  @DisplayName("여러명의 사용자가 한 좌석을 예약 하려고 할 때, 한명만 예약 성공해야 한다")
  public void 좌석_예약_동시성_테스트() throws InterruptedException {

    final int TRY_COUNT = 10000;

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(TRY_COUNT);
    AtomicInteger successCount = new AtomicInteger(0);

    for (int i = 0; i < TRY_COUNT; i++) {
      Member member = memberRepository.save(Member.create("test"));
      Token token = tokenRepository.save(
          new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));
      pointRepository.save(new Point(0L, member.getId(), BigDecimal.valueOf(100000)));
      ReserveConcertSeat.Command command = new ReserveConcertSeat.Command(token.getId(),
          concertSeat.getId());

      executorService.submit(() -> {
        try {
          concertSeatReservationUseCase.reserveConcert(
              command);
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