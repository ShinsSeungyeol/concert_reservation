package study.shinseungyeol.backend.domain.concert;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.shinseungyeol.backend.infra.concert.ConcertRepository;
import study.shinseungyeol.backend.infra.concert.ConcertScheduleRepository;
import study.shinseungyeol.backend.infra.concert.ConcertSeatRepository;

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

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      concertService.getAvailableConcertSeats(1L);
    });
  }

  @Test
  public void 콘서트_예약_가능_일자_조회_콘서트_없는_경우() {
    when(concertRepository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      concertService.getAvailableConcertSchedules(1L);
    });
  }

  @Test
  void 예약_가능한_좌석_조회_콘서트_좌석이_존재하지_않는_경우() {
    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      concertService.checkAvailableConcertSeatWithLock(1L);
    });
  }

  @Test
  void 예약_가능한_좌석_조회_콘서트_좌석이_available_상태가_아닌_경우() {
    Concert concert = Concert.create("test");
    ConcertSchedule concertSchedule = ConcertSchedule.create(concert, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L));
    ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule, 1, BigDecimal.TEN, false);

    when(concertSeatRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(concertSeat));

    Assertions.assertThrows(IllegalStateException.class, () -> {
      concertService.checkAvailableConcertSeatWithLock(1L);
    });
  }


}