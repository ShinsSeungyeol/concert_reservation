package study.shinseungyeol.backend.domain.point;

import java.util.Optional;

public interface PointRepository {

  Point save(Point point);

  Optional<Point> findByMemberIdForUpdate(Long memberId);

  Optional<Point> findByMemberId(Long memberId);

  void deleteAll();
}
