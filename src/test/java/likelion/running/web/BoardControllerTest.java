package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.service.BoardService;
import likelion.running.web.dto.boardDto.BoardForm;
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
        BoardForm board = new BoardForm();
        board.setTitle("test");
        //when
        Optional<Board> result = boardService.openRunning(board);
        //then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getTitle()).isEqualTo("test");
    }

}