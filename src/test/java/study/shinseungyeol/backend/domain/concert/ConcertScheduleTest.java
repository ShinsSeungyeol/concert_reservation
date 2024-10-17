package study.shinseungyeol.backend.domain.concert;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConcertScheduleTest {

  private Concert concert;
  private LocalDateTime startAt;
  private LocalDateTime endAt;

  @BeforeEach
  public void setUp() {
    concert = new Concert(1L, "TEST");
    startAt = LocalDateTime.now();
    endAt = LocalDateTime.now().plusDays(1);
  }

  @Test
  public void 팩토리_동작_정상_테스트() {
    ConcertSchedule concertSchedule = ConcertSchedule.create(concert, startAt, endAt);

    Assertions.assertEquals(concert, concertSchedule.getConcert());
    Assertions.assertEquals(startAt, concertSchedule.getStartAt());
    Assertions.assertEquals(endAt, concertSchedule.getEndAt());
  }


  @Test
  public void 팩토리_startAt_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSchedule.create(concert, null, endAt);
    });
  }

  @Test
  public void 팩토리_endAt_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSchedule.create(concert, startAt, null);
    });
  }

}