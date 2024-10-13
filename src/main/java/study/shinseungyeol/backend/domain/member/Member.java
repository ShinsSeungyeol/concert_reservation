package study.shinseungyeol.backend.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.shinseungyeol.backend.domain.BaseEntity;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 20)
  private String name;

  /**
   * Member 정적 팩터리 메서드
   *
   * @param name
   * @return
   */
  public static Member create(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Member name cannot be null or blank");
    }

    Member member = new Member();
    member.setName(name);

    return member;
  }
}
