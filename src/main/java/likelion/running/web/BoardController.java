package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.service.BoardService;
import likelion.running.web.dto.boardDto.BoardForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Controller
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


}
