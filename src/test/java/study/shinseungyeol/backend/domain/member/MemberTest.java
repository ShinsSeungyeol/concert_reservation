package study.shinseungyeol.backend.domain.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  @DisplayName("정적 팩토리 메서드는 name이 null인 경우 생성이 안되어야 함")
  public void 정적팩토리_name_null인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Member.create(null));
  }

  @Test
  @DisplayName("정적 팩토리 메서드는 name이 공백인 경우 생성이 안되어야 함")
  public void 정적팩토리_name_blank인_경우() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Member.create("         "));
  }

}