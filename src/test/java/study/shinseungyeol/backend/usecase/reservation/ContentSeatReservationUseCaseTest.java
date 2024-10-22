package study.shinseungyeol.backend.usecase.reservation;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.shinseungyeol.backend.domain.concert.Concert;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.member.Member;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.infra.concert.ConcertJPARepository;
import study.shinseungyeol.backend.infra.concert.ConcertScheduleJPARepository;
import study.shinseungyeol.backend.infra.concert.ConcertSeatJPARepository;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.infra.reservation.ConcertSeatReservationRepository;
import study.shinseungyeol.backend.infra.token.TokenRepository;

@SpringBootTest
@Transactional
class ContentSeatReservationUseCaseTest {

  @Autowired
  private ContentSeatReservationUseCase concertSeatReservationUseCase;
  @Autowired
  private TokenRepository tokenRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ConcertJPARepository concertRepository;
  @Autowired
  private ConcertScheduleJPARepository concertScheduleRepository;
  @Autowired
  private ConcertSeatJPARepository concertSeatRepository;

  private Member member;
  private Concert concert;
  private ConcertSchedule concertSchedule;
  private ConcertSeat concertSeat;
  @Autowired
  private ConcertSeatReservationRepository concertSeatReservationRepository;


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
  public void 콘서트좌석_예약_인액티브_토큰_불가() {
    Token token = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE));

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertSeatReservationUseCase.reserveConcert(token.getId(), concertSeat.getId());
    });
  }

  @Test
  public void 콘서트좌석_예약_대기_토큰_불가() {
    Token token = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING));

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertSeatReservationUseCase.reserveConcert(token.getId(), concertSeat.getId());
    });
  }

  @Test
  public void 콘서트좌석_예약_정상_테스트() {
    Token token = tokenRepository.save(
        new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE));

    concertSeatReservationUseCase.reserveConcert(token.getId(), concertSeat.getId());

    ConcertSeat actual = concertSeatRepository.findById(concertSeat.getId()).orElse(null);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(false, actual.getAvailable());
    Assertions.assertEquals(TokenStatus.INACTIVE, token.getStatus());

  }

  @Test
  public void 만료된_예약_취소_정상_테스트() {
    Member newMember = memberRepository.save(Member.create("test2"));

    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(null,
        newMember.getId(), concertSeat.getId(),
        ReservationStatus.PENDING, LocalDateTime.now());

    concertSeatReservationRepository.save(concertSeatReservation);

    concertSeatReservationUseCase.cancelReservation();

    Assertions.assertEquals(ReservationStatus.CANCELED,
        concertSeatReservation.getReservationStatus());

    ConcertSeat actualConcertSeat = concertSeatRepository.findById(concertSeat.getId())
        .orElse(null);

    Assertions.assertNotNull(actualConcertSeat);
    Assertions.assertEquals(true, actualConcertSeat.getAvailable());

  }
}