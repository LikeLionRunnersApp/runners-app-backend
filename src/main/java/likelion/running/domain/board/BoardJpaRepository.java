package likelion.running.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface BoardJpaRepository extends JpaRepository<Board,Long> {
    Optional<Board> findBoardById(Long boardId);
}
