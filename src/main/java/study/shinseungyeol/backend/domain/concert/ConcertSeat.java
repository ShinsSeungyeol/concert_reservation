package study.shinseungyeol.backend.domain.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConcertSeat extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private ConcertSchedule concertSchedule;

  @Column(nullable = false)
  private Integer seq;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Boolean available = true;


  /**
   * 정적 팩토리 함수
   *
   * @param concertSchedule
   * @param seq
   * @param price
   * @return
   */
  public static ConcertSeat create(ConcertSchedule concertSchedule, Integer seq,
      BigDecimal price) {
    if (concertSchedule == null) {
      throw new IllegalArgumentException("concertSchedule can not be null");
    }

    if (seq == null) {
      throw new IllegalArgumentException("seq can not be null");
    }

    if (price == null) {
      throw new IllegalArgumentException("price can not be null");
    }

    ConcertSeat concertSeat = new ConcertSeat();
    concertSeat.setConcertSchedule(concertSchedule);
    concertSeat.setSeq(seq);
    concertSeat.setPrice(price);

    return concertSeat;
  }
}
