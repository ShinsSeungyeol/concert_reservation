package study.shinseungyeol.backend.infra.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.shinseungyeol.backend.domain.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
