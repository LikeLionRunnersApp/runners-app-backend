package likelion.running.web;

import likelion.running.converter.StringToLocalDate;
import likelion.running.domain.board.Board;
import likelion.running.service.BoardService;
import likelion.running.web.dto.BoolRespose;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {
    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/board")
    public BoolRespose openRun(@RequestBody BoardForm boardForm) {
        Optional<Board> board = boardService.openRunning(boardForm);
        if(board.isEmpty()){
            return BoolRespose.builder()
                    .ok(false)
                    .build();
        }
        return BoolRespose.builder()
                .ok(true)
                .build();
    }

    @GetMapping("/board/{boardId}")
    public Board getBoard(@PathVariable Long boardId) {
        log.info(String.valueOf(boardId));
        return boardService.findByBoardId(boardId).orElse(null);
    }

    @DeleteMapping("/board/{boardId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public String deleteBoard(@PathVariable Long boardId) {
        log.info("delete 시도 {}",boardId);
        return boardService.removeBoard(boardId);
    }

    @PatchMapping("/board/{boardId}")
    public BoolRespose updateBoard(@PathVariable Long boardId, @Valid @RequestBody EditBoardDto editBoardDto) {
        log.info("update 시도 {}",boardId);
        Optional<Board> board = boardService.findByBoardId(boardId);
        if(board.isPresent()){
            String hostId = board.get().getHostId();
            if(editBoardDto.getMemberId().equals(hostId)){
                return boardService.editBoard(boardId, editBoardDto);
            }
        }//모임이 있는지 확인
        return BoolRespose.builder()
                .ok(false)
                .build();
    }

    @GetMapping("/week/{date}")
    public List<Board> getWeek(@PathVariable String date) {
        StringToLocalDate converter = new StringToLocalDate();
        LocalDate time = converter.convert(date);
        return boardService.findAllBoardByTime(time);
    }

    @GetMapping("/coming-soon")
    public List<Board> getComing(@RequestBody HashMap<String, String> memberId) {
        log.info("id {}",memberId);
        String memberName = memberId.get("memberId");
        return boardService.findAllBoardByMemberId(memberName);
    }

}
