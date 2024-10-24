package study.shinseungyeol.backend.domain.reservation;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

class ConcertSeatReservationTest {

  @Test
  public void 팩토리_메소드_정상_동작_테스트() {
    Long memberId = 123L;
    Long concertSeatId = 111L;

    ConcertSeatReservation concertSeatReservation = ConcertSeatReservation.create(memberId,
        concertSeatId);

    Assertions.assertEquals(memberId, concertSeatReservation.getMemberId());
    Assertions.assertEquals(concertSeatId, concertSeatReservation.getConcertSeatId());
    Assertions.assertEquals(ReservationStatus.PENDING,
        concertSeatReservation.getReservationStatus());
    Assertions.assertNotNull(concertSeatReservation.getExpireAt());
  }

  @Test
  public void 팩토리_메서드_memberId_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSeatReservation.create(1L, null);
    });
  }

  @Test
  public void 팩토리_메서드_concertSeatId_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      ConcertSeatReservation.create(null, 1L);
    });
  }

  @Test
  public void 좌석_예약_취소_정상_테스트() {
    ConcertSeatReservation reservation = ConcertSeatReservation.create(1L, 2L);

    reservation.cancel();

    Assertions.assertEquals(ReservationStatus.CANCELED, reservation.getReservationStatus());
  }

  @Test
  public void 좌석_예약_완료_테스트() {
    ConcertSeatReservation reservation = ConcertSeatReservation.create(1L, 2L);

    reservation.complete();

    Assertions.assertEquals(ReservationStatus.COMPLETED, reservation.getReservationStatus());
  }

  @Test
  public void 예약이_만료_되었는을_때_익셉션() {
    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(1L, 1L, 1L,
        ReservationStatus.PENDING, LocalDateTime.now());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertSeatReservation.checkIsExpired();
    });

    Assertions.assertEquals(ErrorCode.EXPIRED_SEAT_RESERVATION, customException.getErrorCode());
  }

  @Test
  public void 예약이_만료_안되었는지_정상동작_테스트() {
    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(1L, 1L, 1L,
        ReservationStatus.PENDING, LocalDateTime.now().plusSeconds(1));

    concertSeatReservation.checkIsExpired();
  }
}