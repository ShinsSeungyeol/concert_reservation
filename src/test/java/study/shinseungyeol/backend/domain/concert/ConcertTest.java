package study.shinseungyeol.backend.domain.concert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConcertTest {

  @Test
  public void 팩토리_함수_정상_테스트() {
    Concert concert = Concert.create("test");

    Assertions.assertEquals(concert.getName(), "test");
  }

  @Test
  public void 팩토리_함수_네임_null인_경우_테스트() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Concert.create(null));
  }

  @Test
  public void 팩토리_함수_네임_공백인_경우_테스트() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Concert.create(""));
  }

  @Test
  public void 팩토리_함수_네임_blank인_경우_테스트() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Concert.create("    "));
  }

}