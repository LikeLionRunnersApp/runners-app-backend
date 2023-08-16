package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.web.dto.boardDto.BoardForm;

import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
public class BoardController {

    private final BoardService boardService;
    private final GuestService guestService;
    @Autowired
    public BoardController(BoardService boardService, GuestService guestService) {
        this.boardService = boardService;
        this.guestService = guestService;
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
        Optional<Board> board = boardService.findByBoardId(boardId);
        if(board.isPresent()){
            String hostId = board.get().getHostId();
            if(editBoardDto.getMemberId().equals(hostId)){
                return boardService.editBoard(boardId, editBoardDto);
            }
        }//모임이 있는지 확인
        return "모임의 호스트가 아닙니다.";
    }

}
