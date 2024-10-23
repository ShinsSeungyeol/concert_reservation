package study.shinseungyeol.backend.infra.point;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.point.Point;
import study.shinseungyeol.backend.domain.point.PointRepository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

  private final PointJpaRepository pointJpaRepository;

  @Override
  public Point save(Point point) {
    return pointJpaRepository.save(point);
  }

  @Override
  public Optional<Point> findByMemberId(Long memberId) {
    return pointJpaRepository.findByMemberId(memberId);
  }

  @Override
  public Optional<Point> findByMemberIdForUpdate(Long memberId) {
    return pointJpaRepository.findByMemberIdForUpdate(memberId);
  }

  @Override
  public void deleteAll() {
    pointJpaRepository.deleteAll();
  }
}
