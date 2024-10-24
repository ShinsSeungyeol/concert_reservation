package study.shinseungyeol.backend.domain.concert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

  @InjectMocks
  private ConcertService concertService;

  @Mock
  private ConcertRepository concertRepository;

  @Mock
  private ConcertScheduleRepository concertScheduleRepository;

  @Mock
  private ConcertSeatRepository concertSeatRepository;

  @Test
  public void 콘서트_예약_가능_좌석_목록_조회_콘서트_없는_경우() {
    when(concertRepository.findById(1L)).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertService.getAvailableConcertSeats(1L);
    });

    Assertions.assertEquals(ErrorCode.NOT_FOUND_CONCERT, customException.getErrorCode());
  }

  @Test
  public void 콘서트_예약_가능_일자_조회_콘서트_없는_경우() {
    when(concertRepository.findById(1L)).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertService.getAvailableConcertSchedules(1L);
    });

    Assertions.assertEquals(ErrorCode.NOT_FOUND_CONCERT, customException.getErrorCode());
  }

  @Test
  public void 예약_가능한_좌석_조회_콘서트_좌석이_존재하지_않는_경우() {
    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertService.checkAvailableConcertSeatWithLock(1L);
    });

    Assertions.assertEquals(ErrorCode.NOT_FOUND_SEAT, customException.getErrorCode());
  }

  @Test
  public void 예약_가능한_좌석_조회_콘서트_좌석이_available_상태가_아닌_경우() {
    Concert concert = Concert.create("test");
    ConcertSchedule concertSchedule = ConcertSchedule.create(concert, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L));
    ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, 1, BigDecimal.TEN, false);

    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(concertSeat));

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertService.checkAvailableConcertSeatWithLock(1L);
    });
  }

  @Test
  public void 콘서트_좌석_점유_ID_없는_경우_익셉션() {
    when(concertSeatRepository.findByIdForUpdate(any(Long.class))).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertService.getConcertSeat(1L);
    });

    Assertions.assertEquals(ErrorCode.NOT_FOUND_SEAT, customException.getErrorCode());
  }

  @Test
  void 콘서트_좌서_점유_정상_테스트() {
    ConcertSeat concertSeat = new ConcertSeat(1L, null, 1, BigDecimal.TEN, true);

    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(concertSeat));

    concertService.convertConcertSeatToOccupied(1L);

    Assertions.assertFalse(concertSeat.getAvailable());
  }

  @Test
  public void 콘서트_좌석_사용_가능_상태로_변경_ID_없는_경우_익셉션() {
    when(concertSeatRepository.findByIdForUpdate(any(Long.class))).thenReturn(Optional.empty());

    CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
      concertService.convertConcertSeatToAvailable(1L);
    });

    Assertions.assertEquals(ErrorCode.NOT_FOUND_SEAT, customException.getErrorCode());
  }

  @Test
  void 콘서트_좌석_사용_가능_상태로_변경_정상_테스트() {
    ConcertSeat concertSeat = new ConcertSeat(1L, null, 1, BigDecimal.TEN, false);

    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(concertSeat));

    concertService.convertConcertSeatToAvailable(1L);

    Assertions.assertTrue(concertSeat.getAvailable());

  }

}