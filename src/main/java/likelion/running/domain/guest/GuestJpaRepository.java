package likelion.running.domain.guest;

import likelion.running.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestJpaRepository extends JpaRepository<Guest,Long> {
    List<Guest> findAllByBoardId(Long boardId);
}
