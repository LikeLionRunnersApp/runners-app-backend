package likelion.running.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryJap extends JpaRepository<Member,Long> {
    Optional<Member>findMemberByMemberId(String memberId);
//    List<Member>findAll(String memberId);
}
