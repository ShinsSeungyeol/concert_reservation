package study.shinseungyeol.backend.domain.point;

public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

  void deleteAll();

}
