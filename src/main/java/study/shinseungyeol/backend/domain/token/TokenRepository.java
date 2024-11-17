package study.shinseungyeol.backend.domain.token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface TokenRepository {

  Token save(Token token);

  Optional<Token> findByMemberIdForUpdate(Long memberId);

  List<Token> findAllByStatusOrderByUpdateAtAsc(TokenStatus status, Pageable pageable);

  Optional<Token> findByIdForUpdate(UUID uuid);

  Optional<Token> findById(UUID uuid);


  void deleteAll();
}
