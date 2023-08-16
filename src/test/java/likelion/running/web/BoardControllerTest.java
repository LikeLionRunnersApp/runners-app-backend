package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.FlagType;
import likelion.running.service.BoardService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.EditBoardDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardControllerTest {

    @Autowired
    BoardService boardService;
    @Test
    public void openRun(){
        //given
        BoardForm board = BoardForm.builder().title("test").build();
        //when
        Optional<Board> result = boardService.openRunning(board);
        //then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getTitle()).isEqualTo("test");
    }

    @Test
    public void findBoard(){
        //given
        BoardForm b1 = BoardForm.builder()
                .title("mooyaho")
                .hostId("test1@naver.com")
                .build();
        Optional<Board> board = boardService.openRunning(b1);
        Assertions.assertThat(board).isPresent();

        //when
        Optional<Board> byBoardId = boardService.findByBoardId(board.get().getId());

        //then
        Assertions.assertThat(byBoardId).isPresent();
        Assertions.assertThat(board.get().getTitle()).isEqualTo(byBoardId.get().getTitle());

    }

    @Test
    public void deleteBoard(){
        //given
        BoardForm b1 = BoardForm.builder()
                .title("mooyaho")
                .hostId("test1@naver.com")
                .build();

        Optional<Board> board1 = boardService.openRunning(b1);
        //when
        Assertions.assertThat(board1).isPresent();
        String result = boardService.removeBoard(board1.get().getId());

        //then
        Assertions.assertThat(result).isEqualTo("ok");
        Assertions.assertThat(boardService.findByBoardId(board1.get().getId())).isEmpty();
    }

    @Test
    public void updateBoard(){
        //given
        BoardForm b1 = BoardForm.builder()
                .hostId("test1@naver.com")
                .title("mooyaho")
                .build();

        Optional<Board> board = boardService.openRunning(b1);
        Assertions.assertThat(board).isPresent();
        EditBoardDto build = EditBoardDto.builder().title("test")
                .content("test start")
                .place("korea")
                .flag(FlagType.INTERVAL)
                .runTime("3")
                .walkTime("1")
                .time("2023-08-14")
                .totalMember(6)
                .play_time(40)
                .build();
        Assertions.assertThat(board).isPresent();
        Long id = board.get().getId();
        //when
        String result = boardService.editBoard(board.get().getId(), build);

        //then
        Optional<Board> b = boardService.findByBoardId(id);
        Assertions.assertThat(b).isPresent();
        Assertions.assertThat(result).isEqualTo("ok");
        Assertions.assertThat(build.getContent()).isEqualTo(b.get().getContent());
    }
}
