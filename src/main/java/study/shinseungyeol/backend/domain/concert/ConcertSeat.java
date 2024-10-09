package study.shinseungyeol.backend.domain.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeat extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Concert concert;

  private boolean empty = true;

  /**
   * 콘서트 좌석 정정 팩토리 함수
   *
   * @param name
   * @param concert
   * @return
   */
  public static ConcertSeat create(String name, Concert concert) {
    ConcertSeat seat = new ConcertSeat();
    seat.setName(name);
    seat.setConcert(concert);

    concert.addSeat(seat);
    return seat;
  }
}
