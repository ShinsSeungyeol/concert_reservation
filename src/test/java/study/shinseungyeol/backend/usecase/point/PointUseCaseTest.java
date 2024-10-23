package study.shinseungyeol.backend.usecase.point;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import study.shinseungyeol.backend.domain.point.PointRepository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationRepository;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationService;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.usecase.point.dto.ChargePoint;
import study.shinseungyeol.backend.usecase.point.dto.UsePoint.Command;

@SpringBootTest
@Transactional
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


  private Member member;
  private Point point;
  private Concert concert;
  private ConcertSchedule concertSchedule;
  private ConcertSeat concertSeat;
  private Long reservationId;



  @BeforeEach
  public void setUp() {
    member = memberRepository.save(Member.create("신승열"));
    point = pointRepository.save(new Point(null, member.getId(), BigDecimal.valueOf(1000)));
    concert = concertRepository.save(Concert.create("TESTCONCERT"));
    concertSchedule = concertScheduleRepository.save(
        ConcertSchedule.create(concert, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
    concertSeat = concertSeatRepository.save(
        ConcertSeat.create(concertSchedule, 1, BigDecimal.TEN));
    reservationId = concertSeatReservationService.createConcertSeatReservation(member.getId(),
        concertSeat.getId()).getId();
  }

  public void 포인트사용_인액티브_토큰은_불가() {
    Token inactiveToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE));

    Command command = new Command(inactiveToken.getId(), reservationId);

    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.usePointWithValidateToken(command));
  }

  @Test
  public void 포인트사용_대기_토큰은_불가() {
    Token pendingToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING));

    Command command = new Command(pendingToken.getId(), reservationId);

    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.usePointWithValidateToken(command));
  }

  @Test
  public void 포인트_사용_예약_내역_없는_경우_에러() {
    Token activeToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));

    Command command = new Command(activeToken.getId(), 300L);

    Assertions.assertThrows(NoSuchElementException.class,
        () -> pointUseCase.usePointWithValidateToken(command));
  }

  @Test
  public void 포인트_사용_정상_동작_테스트() {
    Token activeToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));

    Command command = new Command(activeToken.getId(), reservationId);

    BigDecimal initBalance = point.getBalanceAmount();

    BigDecimal actual = pointUseCase.usePointWithValidateToken(command).getBalance();

    Assertions.assertEquals(
        initBalance.subtract(concertSeat.getPrice()), actual);

    List<ConcertSeatReservation> reservations = concertSeatReservationRepository.findAll()
        .stream()
        .filter(reservation -> reservation.getMemberId().equals(member.getId()))
        .toList();

    Assertions.assertEquals(reservations.get(0).getReservationStatus(),
        ReservationStatus.COMPLETED);
  }

  @Test
  public void 포인트충전_인액티브_토큰은_불가() {
    Token inactiveToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE));

    ChargePoint.Command command = new ChargePoint.Command(inactiveToken.getId(), BigDecimal.TEN);

    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.chargePointWithValidateToken(command));
  }

  @Test
  public void 포인트충전_대기_토큰은_불가() {
    Token pendingToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING));

    ChargePoint.Command command = new ChargePoint.Command(pendingToken.getId(), BigDecimal.TEN);

    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.chargePointWithValidateToken(command));
  }

  @Test
  public void 포인트충전_정상_동작() {
    Token activeToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));
    BigDecimal init = point.getBalanceAmount();
    BigDecimal charge = BigDecimal.TEN;

    ChargePoint.Command command = new ChargePoint.Command(activeToken.getId(), charge);

    pointUseCase.chargePointWithValidateToken(command);

    Assertions.assertEquals(init.add(charge).compareTo(point.getBalanceAmount()), 0);
  }

  @Test
  public void 포인트조회_인액티브_토큰은_불가() {
    Token inactiveToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE));

    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.getPointAmountWithValidateToken(inactiveToken.getId()));
  }

  @Test
  public void 포인트조회_대기_토큰은_불가() {
    Token pendingToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING));
    Assertions.assertThrows(IllegalStateException.class,
        () -> pointUseCase.getPointAmountWithValidateToken(pendingToken.getId()));
  }

  @Test
  public void 포인트조회_정상_동작() {
    Token activeToken = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));

    BigDecimal expect = point.getBalanceAmount();

    BigDecimal actual = pointUseCase.getPointAmountWithValidateToken(activeToken.getId())
        .getBalance();

    Assertions.assertEquals(expect, actual);
  }

}