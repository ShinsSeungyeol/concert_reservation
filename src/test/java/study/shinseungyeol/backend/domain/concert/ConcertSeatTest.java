package study.shinseungyeol.backend.domain.concert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConcertSeatTest {

  private Concert concert;
  private ConcertSchedule concertSchedule;

  @BeforeEach
  public void setUp() {
    concert = new Concert(1L, "test concert");
    concertSchedule = new ConcertSchedule(1L, concert, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1));
  }

  @Test
  public void 팩토리_정상_동작_테스트() {
    int seq = 22;
    BigDecimal price = BigDecimal.valueOf(3000);
    ConcertSeat concertSeat = ConcertSeat.create(concertSchedule, seq, price);

    Assertions.assertEquals(concertSchedule, concertSeat.getConcertSchedule());
    Assertions.assertEquals(seq, concertSeat.getSeq());
    Assertions.assertEquals(price, concertSeat.getPrice());
  }

  @Test
  public void 팩토리_concertSchedule_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSeat.create(null, 0, BigDecimal.TEN);
    });
  }

  @Test
  public void 팩토리_seq_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSeat.create(concertSchedule, null, BigDecimal.TEN);
    });
  }

  @Test
  public void 팩토리_price인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSeat.create(concertSchedule, 0, null);
    });
  }

  @Test
  public void 점유_상태_변경_테스트() {
    ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, 2, BigDecimal.TEN, true);

    concertSeat.occupied();

    Assertions.assertFalse(concertSeat.getAvailable());
  }

  @Test
  public void 좌석_사용_가능_변경_테스트() {
    ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, 2, BigDecimal.TEN, false);

    concertSeat.available();

    Assertions.assertTrue(concertSeat.getAvailable());

  }
}