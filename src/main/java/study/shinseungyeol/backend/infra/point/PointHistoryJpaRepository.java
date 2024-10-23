package study.shinseungyeol.backend.infra.point;

import org.springframework.data.jpa.repository.JpaRepository;
import study.shinseungyeol.backend.domain.point.PointHistory;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

}
