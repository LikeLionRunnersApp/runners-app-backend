package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.service.BoardService;
import likelion.running.web.dto.boardDto.BoardForm;

import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
public class BoardController {

    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/board")
    public boolean openRun(@RequestBody BoardForm boardForm){
        Optional<Board> board = boardService.openRunning(boardForm);
        return board.isPresent();
    }

    @GetMapping("/board/{boardId}")
    public Board getBoard(@PathVariable Long boardId){
        log.info(String.valueOf(boardId));
        return boardService.findByBoardId(boardId).orElse(null);
    }

    @DeleteMapping("/board/{boardId}")
    public String deleteBoard(@PathVariable Long boardId){
        log.info("delete 시도 {}",boardId);
        return boardService.removeBoard(boardId);
    }

    @PatchMapping("/board/{boardId}")
    public String updateBoard(@PathVariable Long boardId, @Valid @RequestBody EditBoardDto editBoardDto){
        log.info("update 시도 {}",boardId);
        return boardService.editBoard(boardId, editBoardDto);
    }

}
