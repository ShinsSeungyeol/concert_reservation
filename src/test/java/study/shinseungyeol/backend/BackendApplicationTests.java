package study.shinseungyeol.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaAuditing
@EnableJpaRepositories
class BackendApplicationTests {

  @Test
  void contextLoads() {
  }

}
