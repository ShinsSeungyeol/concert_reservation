package study.shinseungyeol.backend.domain.concert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String name;

  @Column
  private BigDecimal price;

  @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  private List<ConcertSeat> seats = new ArrayList<>();

  private LocalDateTime reservationStartAt;

  private LocalDateTime reservationEndAt;

  private LocalDateTime startAt;

  private LocalDateTime endAt;


  /**
   * 콘서트 정적 팩토리 함수
   *
   * @param name
   * @param price
   * @param reservationStartAt
   * @param reservationEndAt
   * @param startAt
   * @param endAt
   * @return
   */
  public static Concert create(String name, BigDecimal price, LocalDateTime reservationStartAt,
      LocalDateTime reservationEndAt, LocalDateTime startAt, LocalDateTime endAt) {
    Concert concert = new Concert();
    concert.setName(name);
    concert.setPrice(price);
    concert.setReservationStartAt(reservationStartAt);
    concert.setReservationEndAt(reservationEndAt);
    concert.setStartAt(startAt);
    concert.setEndAt(endAt);

    return concert;
  }


  /**
   * 콘서트 좌석 추가
   *
   * @param seat
   */
  protected void addSeat(ConcertSeat seat) {
    this.seats.add(seat);
  }
}
