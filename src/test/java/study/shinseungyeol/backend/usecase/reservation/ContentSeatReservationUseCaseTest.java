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
import study.shinseungyeol.backend.domain.concert.ConcertRepository;
import study.shinseungyeol.backend.domain.concert.ConcertSchedule;
import study.shinseungyeol.backend.domain.concert.ConcertScheduleRepository;
import study.shinseungyeol.backend.domain.concert.ConcertSeat;
import study.shinseungyeol.backend.domain.concert.ConcertSeatRepository;
import study.shinseungyeol.backend.domain.member.Member;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservation;
import study.shinseungyeol.backend.domain.reservation.ConcertSeatReservationRepository;
import study.shinseungyeol.backend.domain.reservation.ReservationStatus;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.usecase.reservation.dto.ReserveConcertSeat;

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
  private ConcertRepository concertRepository;
  @Autowired
  private ConcertScheduleRepository concertScheduleRepository;
  @Autowired
  private ConcertSeatRepository concertSeatRepository;
  @Autowired
  private ConcertSeatReservationRepository concertSeatReservationRepository;

  private Member member;
  private Concert concert;
  private ConcertSchedule concertSchedule;
  private ConcertSeat concertSeat;



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