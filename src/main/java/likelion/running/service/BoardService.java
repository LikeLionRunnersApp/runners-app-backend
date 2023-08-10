package likelion.running.service;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.web.dto.boardDto.BoardForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;
    @Autowired
    public BoardService(BoardJpaRepository boardJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
    }

    public Optional<Board> openRunning(BoardForm boardForm){
        Board board = boardJpaRepository.save(Board.builder()
                .hostId(boardForm.getHostId())
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .totalMember(boardForm.getTotalMember())
                .runningType(boardForm.getRunningType())
                .runTime(boardForm.getRunTime())
                .walkTime(boardForm.getWalkTime())
                .fullTime(boardForm.getFullTime())
                .build());
        log.info("board 생성 {}",board);
        return Optional.of(board);
    }

}
