package likelion.running.domain.board;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface BoardJpaRepository extends JpaRepository<Board,Long> {
    Optional<Board> findBoardById(Long boardId);
    Optional<Board> findBoardByHostId(String hostId);
    List<Board> findAllByNormalTime(LocalDate time);
    @EntityGraph(attributePaths = "guests")
    Optional<Board> findById(Long Id);

    List<Board> findAllByHostIdAndNormalTimeIsAfter(String hostId, LocalDate time);
}
