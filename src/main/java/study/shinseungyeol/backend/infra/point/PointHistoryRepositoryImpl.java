package study.shinseungyeol.backend.infra.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.point.PointHistory;
import study.shinseungyeol.backend.domain.point.PointHistoryRepository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

  private final PointHistoryJpaRepository pointHistoryJpaRepository;

  @Override
  public PointHistory save(PointHistory pointHistory) {
    return pointHistoryJpaRepository.save(pointHistory);
  }

  @Override
  public void deleteAll() {
    pointHistoryJpaRepository.deleteAll();
  }
}
