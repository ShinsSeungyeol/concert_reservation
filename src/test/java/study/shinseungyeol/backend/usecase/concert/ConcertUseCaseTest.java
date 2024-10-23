package study.shinseungyeol.backend.usecase.concert;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;
import study.shinseungyeol.backend.infra.member.MemberRepository;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSchedule.Query;
import study.shinseungyeol.backend.usecase.concert.dto.AvailableConcertSeat;

@SpringBootTest
@Transactional
class ConcertUseCaseTest {

  @Autowired
  private ConcertUseCase concertUseCase;
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

  private Member member;
  private Concert concert;
  private List<ConcertSchedule> availableConcertSchedules;
  private List<ConcertSchedule> occupiedConcertSchedules;
  private List<ConcertSeat> concertSeats;



  @BeforeEach
  public void setUp() {
    member = memberRepository.save(Member.create("신승열"));
    concert = concertRepository.save(Concert.create("test concert"));
    availableConcertSchedules = new ArrayList<>();
    occupiedConcertSchedules = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      availableConcertSchedules.add(concertScheduleRepository.save(
          ConcertSchedule.create(concert, LocalDateTime.now(), LocalDateTime.now().plusDays(1))));

      occupiedConcertSchedules.add(concertScheduleRepository.save(
          ConcertSchedule.create(concert, LocalDateTime.now(), LocalDateTime.now().plusDays(2))));
    }

    concertSeats = new ArrayList<>();

    availableConcertSchedules.stream().forEach(concertSchedule ->
    {
      for (int i = 0; i < 50; i++) {
        concertSeats.add(
            concertSeatRepository.save(ConcertSeat.create(concertSchedule, i, BigDecimal.TEN)));
      }
    });

    occupiedConcertSchedules.stream().forEach(concertSchedule -> {
      for (int i = 0; i < 50; i++) {
        concertSeats.add(
            concertSeatRepository.save(
                new ConcertSeat(null, concertSchedule, i, BigDecimal.TEN, false)));
      }
    });

  }

  @Test
  public void 예약_가능_날짜_토큰_대기_상태_사용자_이용_불가() {
    Token pendingToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING);
    tokenRepository.save(pendingToken);

    Query query = new Query(pendingToken.getId(), concert.getId());

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertUseCase.getAvailableConcertSchedules(query);
    });
  }

  @Test
  public void 예약_가능_날짜_토큰_인액티브_상태_사용자_이용_불가() {
    Token inactiveToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE);
    tokenRepository.save(inactiveToken);

    Query query = new Query(inactiveToken.getId(), concert.getId());

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertUseCase.getAvailableConcertSchedules(query);
    });
  }

  @Test
  public void 예약_가능_날짜_조회_정상_동작_테스트() {
    Token activeToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE);
    tokenRepository.save(activeToken);

    Query query = new Query(activeToken.getId(), concert.getId());

    List<Long> actual = concertUseCase.getAvailableConcertSchedules(
        query).stream().map(queryResult -> queryResult.getConcertId()).toList();

    Assertions.assertEquals(availableConcertSchedules.size(), actual.size());

    availableConcertSchedules.stream().map(ConcertSchedule::getId).forEach(
        expect -> Assertions.assertTrue(actual.contains(expect))
    );


  }

  @Test
  public void 예약_가능_좌석_대기_상태_사용자_이용_불가() {
    Token pendingToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.PENDING);
    tokenRepository.save(pendingToken);

    AvailableConcertSeat.Query query = new AvailableConcertSeat.Query(pendingToken.getId(),
        concert.getId());

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertUseCase.getAvailableConcertSeats(query);
    });
  }

  @Test
  public void 예약_가능_좌석_인액티브_상태_사용자_이용_불가() {
    Token inactiveToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.INACTIVE);
    tokenRepository.save(inactiveToken);

    AvailableConcertSeat.Query query = new AvailableConcertSeat.Query(inactiveToken.getId(),
        concert.getId());

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertUseCase.getAvailableConcertSeats(query);
    });
  }

  @Test
  public void 예약_가능_좌석_조회() {
    Token activeToken = new Token(UUID.randomUUID(), member.getId(), TokenStatus.ACTIVE);
    tokenRepository.save(activeToken);

    AvailableConcertSeat.Query query = new AvailableConcertSeat.Query(activeToken.getId(),
        concert.getId());

    List<AvailableConcertSeat.QueryResult> actual = concertUseCase.getAvailableConcertSeats(query);

    long expect = concertSeats.stream().filter(ConcertSeat::getAvailable).count();

    Assertions.assertEquals(expect, actual.size());
  }
}