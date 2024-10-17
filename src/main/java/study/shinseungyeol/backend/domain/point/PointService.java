package study.shinseungyeol.backend.domain.point;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.shinseungyeol.backend.infra.point.PointHistoryRepository;
import study.shinseungyeol.backend.infra.point.PointRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

  private final PointRepository pointRepository;
  private final PointHistoryRepository pointHistoryRepository;

  /**
   * 해당 memberId의 멤버 포인트를 사용하는 함수
   *
   * @param memberId
   * @param amount
   */
  public BigDecimal usePoint(Long memberId, BigDecimal amount) {
    Point point = pointRepository.findByMemberIdForUpdate(memberId)
        .orElseThrow(NoSuchElementException::new);

    point.usePoint(amount);

    PointHistory pointHistory = PointHistory.create(point, PointHistoryType.USE, amount);
    pointHistoryRepository.save(pointHistory);

    return point.getBalanceAmount();
  }

  /**
   * 해당 멤버 아이디에 속하는 멤버 포인트 충전하는 함수
   *
   * @param memberId
   * @param amount
   */
  public BigDecimal chargePoint(Long memberId, BigDecimal amount) {
    Point point = pointRepository.findByMemberIdForUpdate(memberId)
        .orElseThrow(NoSuchElementException::new);

    point.addPoint(amount);

    PointHistory pointHistory = PointHistory.create(point, PointHistoryType.CHARGING, amount);
    pointHistoryRepository.save(pointHistory);

    return point.getBalanceAmount();
  }

  /**
   * 해당 포인트 조회
   *
   * @param memberId
   * @return
   */
  public BigDecimal getPointByMemberId(Long memberId) {
    return pointRepository.findByMemberId(memberId).map(Point::getBalanceAmount)
        .orElseThrow(NoSuchElementException::new);
  }
}
