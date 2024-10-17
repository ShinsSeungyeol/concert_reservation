package study.shinseungyeol.backend.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.concert.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
