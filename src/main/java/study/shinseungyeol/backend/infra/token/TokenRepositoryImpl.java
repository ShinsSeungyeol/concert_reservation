package study.shinseungyeol.backend.infra.token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.token.Token;
import study.shinseungyeol.backend.domain.token.TokenRepository;
import study.shinseungyeol.backend.domain.token.TokenStatus;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

  private final TokenJPARepository tokenJPARepository;

  @Override
  public Token save(Token token) {
    return tokenJPARepository.save(token);
  }

  @Override
  public Optional<Token> findByMemberIdForUpdate(Long memberId) {
    return tokenJPARepository.findByMemberIdForUpdate(memberId);
  }

  @Override
  public List<Token> findAllByStatusOrderByUpdateAtAsc(TokenStatus status, Pageable pageable) {
    return tokenJPARepository.findAllByStatusOrderByUpdateAtAsc(status, pageable);
  }

  @Override
  public Optional<Token> findByIdForUpdate(UUID uuid) {
    return tokenJPARepository.findByIdForUpdate(uuid);
  }

  @Override
  public void deleteAll() {
    tokenJPARepository.deleteAll();
  }
}
