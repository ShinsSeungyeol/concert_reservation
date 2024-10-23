package study.shinseungyeol.backend.infra.token;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenStatus;

@Repository
public interface TokenJPARepository extends JpaRepository<Token, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Token t where t.memberId= :memberId")
  Optional<Token> findByMemberIdForUpdate(Long memberId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Token t WHERE t.status = :status ORDER BY t.updateAt ASC ")
  List<Token> findAllByStatusOrderByUpdateAtAsc(@Param("status") TokenStatus tokenStatus,
      Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Token t where t.id = :id")
  Optional<Token> findByIdForUpdate(UUID id);
}

