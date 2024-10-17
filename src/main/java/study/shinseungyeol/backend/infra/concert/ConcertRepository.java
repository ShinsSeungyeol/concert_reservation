package study.shinseungyeol.backend.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shinseungyeol.backend.domain.concert.Concert;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
