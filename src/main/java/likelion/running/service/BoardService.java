package likelion.running.service;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .flag(boardForm.getFlag())
                .runTime(boardForm.getRunTime())
                .walkTime(boardForm.getWalkTime())
                .play_time(boardForm.getPlay_time())
                .status(boardForm.getStatus())
                .build());
        log.info("board 생성 {}",board.getTitle());
        return Optional.of(board);
    }

    public Optional<Board> findByBoardId(Long boardId){
        return boardJpaRepository.findBoardById(boardId);
    }

    public Long findByHostId(String hostId){
        Optional<Board> board = boardJpaRepository.findBoardByHostId(hostId);
        board.ifPresent(value -> log.info(String.valueOf(value.getId())));

        return board.map(Board::getId).orElse(null);
    }
    public String removeBoard(Long boardId){
        boardJpaRepository.deleteById(boardId);
        return "ok";
    }

    @Transactional
    public String editBoard(Long boardId, EditBoardDto editBoardDto){
        Board board = boardJpaRepository.findBoardById(boardId).orElseThrow();
        EditBoardDto.EditBoardDtoBuilder exist = board.toEditor();
        EditBoardDto editNew = exist.title(editBoardDto.getTitle())
                .content(editBoardDto.getContent())
                .flag(editBoardDto.getFlag())
                .place(editBoardDto.getPlace())
                .runTime(editBoardDto.getRunTime())
                .walkTime(editBoardDto.getWalkTime())
                .time(editBoardDto.getTime())
                .totalMember(editBoardDto.getTotalMember())
                .play_time(editBoardDto.getPlay_time())
                .status(editBoardDto.getStatus())
                .build();
        board.edit(editNew);
        log.info("board title {}", board.getTitle());
        log.info("board content {}", board.getContent());
        return "ok";
    }

}
