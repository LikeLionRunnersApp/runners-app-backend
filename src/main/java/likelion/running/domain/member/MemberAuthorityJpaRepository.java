package likelion.running.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAuthorityJpaRepository extends JpaRepository<MemberAuthority,Long> {
}
