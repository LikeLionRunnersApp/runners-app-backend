package likelion.running.domain.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    Optional<Member>findMemberByMemberId(String memberId);
    Optional<Member>findMemberById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByMemberId(String memberId);
}
