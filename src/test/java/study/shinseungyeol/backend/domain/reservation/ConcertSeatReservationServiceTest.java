package study.shinseungyeol.backend.domain.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.shinseungyeol.backend.infra.reservation.ConcertSeatReservationRepository;

@ExtendWith(MockitoExtension.class)
class ConcertSeatReservationServiceTest {

  @InjectMocks
  private ConcertSeatReservationService concertSeatReservationService;

  @Mock
  private ConcertSeatReservationRepository concertSeatReservationRepository;

  private List<ConcertSeatReservation> reservationsAfterLimit;



  @BeforeEach
  public void setUp() {
    Long id = 1L;
    Long memberId = 1L;
    Long seatId = 1L;

    reservationsAfterLimit = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(id, memberId++,
          seatId++, ReservationStatus.PENDING, LocalDateTime.now().minusSeconds(1));

      reservationsAfterLimit.add(concertSeatReservation);
    }

  }


  @Test
  public void 예약_취소_정상_동작_테스트() {
    when(concertSeatReservationRepository.findExpiredAll(eq(ReservationStatus.PENDING),
        any(LocalDateTime.class))).thenReturn(
        reservationsAfterLimit);

    List<Long> canceledSeatIds = concertSeatReservationService.cancelPendingReservationAndGetSeatIdsPerInterval();

    Assertions.assertEquals(reservationsAfterLimit.size(), canceledSeatIds.size());

    reservationsAfterLimit.stream().forEach(
        reservationAfterLimit -> {
          Assertions.assertTrue(canceledSeatIds.contains(reservationAfterLimit.getId()));
        }
    );

    reservationsAfterLimit.stream().forEach(
        reservationAfterLimit -> {
          Assertions.assertEquals(reservationAfterLimit.getReservationStatus(),
              ReservationStatus.CANCELED);
        }
    );
  }

  @Test
  public void 좌석_예약_완료_예약_없는_경우() {
    when(concertSeatReservationRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      concertSeatReservationService.completeConcertSeatReservation(1L);
    });
  }

  @Test
  public void 좌석_예약_완료_정상_동작_테스트() {
    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation(1L, 1L, 1L,
        ReservationStatus.PENDING, LocalDateTime.now().plusMinutes(1));

    when(concertSeatReservationRepository.findByIdForUpdate(1L)).thenReturn(
        Optional.of(concertSeatReservation));

    concertSeatReservationService.completeConcertSeatReservation(1L);

    Assertions.assertEquals(ReservationStatus.COMPLETED,
        concertSeatReservation.getReservationStatus());

  }

}