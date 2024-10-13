package study.shinseungyeol.backend.infra.point;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.point.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Point p where p.memberId = :memberId")
  Optional<Point> findByMemberIdForUpdate(@Param("memberId") Long memberId);


  Optional<Point> findByMemberId(Long memberId);

}
