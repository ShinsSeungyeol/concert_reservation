package study.shinseungyeol.backend.domain.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"concertSeatId", "memberId", "reservationStatus"})}
)
public class ConcertSeatReservation extends BaseEntity {

  @Transient
  private static final long LIMIT_PENDING_MINUTES = 5;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long memberId;
  @Column(nullable = false)
  private Long concertSeatId;
  @Enumerated(EnumType.STRING)
  private ReservationStatus reservationStatus = ReservationStatus.PENDING;
  @Column(nullable = false)
  private LocalDateTime expireAt;

  /**
   * 팩토리 메서드
   *
   * @param memberId
   * @param concertSeatId
   * @return
   */
  public static ConcertSeatReservation create(Long memberId, Long concertSeatId) {
    if (memberId == null) {
      throw new IllegalArgumentException("member cannot be null");
    }

    if (concertSeatId == null) {
      throw new IllegalArgumentException("concertSeat cannot be null");
    }

    ConcertSeatReservation concertSeatReservation = new ConcertSeatReservation();
    concertSeatReservation.setMemberId(memberId);
    concertSeatReservation.setConcertSeatId(concertSeatId);
    concertSeatReservation.setExpireAt(LocalDateTime.now().plusMinutes(LIMIT_PENDING_MINUTES));

    return concertSeatReservation;
  }

  /**
   * 좌석 예약 취소하는 함수
   */
  public void cancel() {
    setReservationStatus(ReservationStatus.CANCELED);
  }

  /**
   * 좌석 예약 완료시키는 함수
   */
  public void complete() {
    if (getReservationStatus() == ReservationStatus.COMPLETED) {
      throw new CustomException(ErrorCode.DUPLICATED_PAYMENT);
    }
    setReservationStatus(ReservationStatus.COMPLETED);
  }

  /**
   * 예약 만료되었는지 리턴
   */
  public void checkIsExpired() {

    if (LocalDateTime.now().isAfter(expireAt)) {
      throw new CustomException(ErrorCode.EXPIRED_SEAT_RESERVATION);
    }
  }
}
