package study.shinseungyeol.backend.domain.concert;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Concert extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(length = 50, nullable = false)
  private String name;

  /**
   * 정적 팩토리 함수
   *
   * @param name
   * @return
   */
  public static Concert create(String name) {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("name can not be null");
    }

    Concert concert = new Concert();
    concert.setName(name);

    return concert;
  }
}
