package study.shinseungyeol.backend.domain.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class ConcertSchedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Concert concert;

  @Column(nullable = false)
  private LocalDateTime startAt;

  @Column(nullable = false)
  private LocalDateTime endAt;

  public static ConcertSchedule create(Concert concert, LocalDateTime startAt,
      LocalDateTime endAt) {
    if (concert == null) {
      throw new IllegalArgumentException("concert must not be null");
    }

    if (startAt == null) {
      throw new IllegalArgumentException("startAt must not be null");
    }

    if (endAt == null) {
      throw new IllegalArgumentException("endAt must not be null");
    }

    ConcertSchedule schedule = new ConcertSchedule();
    schedule.concert = concert;
    schedule.startAt = startAt;
    schedule.endAt = endAt;

    return schedule;
  }
}
