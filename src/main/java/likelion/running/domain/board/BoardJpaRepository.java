package likelion.running.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface BoardJpaRepository extends JpaRepository<Board,Long> {
    Optional<Board> findBoardById(Long boardId);
    Optional<Board> findBoardByHostId(String hostId);
    List<Board> findAllByTime(LocalDate time);
    List<Board> findByHostId(String hostId);

    List<Board> findAllByHostIdAndTimeIsAfter(String hostId,LocalDate time);
}
