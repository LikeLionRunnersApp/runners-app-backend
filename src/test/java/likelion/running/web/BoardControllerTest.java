package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.participate.ParticipateResult;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.EditBoardDto;
import likelion.running.web.dto.memberDto.GuestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardControllerTest {

    @Autowired
    BoardService boardService;
    @Autowired
    GuestService guestService;
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
                .time(LocalDate.of(2023,8,17))
                .totalMember(6)
                .play_time(40)
                .build();
        Assertions.assertThat(board).isPresent();
        Long id = board.get().getId();
        //when
        boolean result = boardService.editBoard(board.get().getId(), build);

        //then
        Optional<Board> b = boardService.findByBoardId(id);
        Assertions.assertThat(b).isPresent();
        Assertions.assertThat(result).isEqualTo(true);
        Assertions.assertThat(build.getContent()).isEqualTo(b.get().getContent());
    }

    @Test
    public void increase(){
        //given
        BoardForm form = BoardForm.builder()
                .title("test")
                .content("testtest")
                .hostId("host@naver.com")
                .totalMember(6)
                .build();
        Optional<Board> board = boardService.openRunning(form);
        Assertions.assertThat(board).isPresent();

        GuestDto build = GuestDto.builder()
                .boardId(board.get().getId())
                .memberId("teststt@nnn.com").build();
        GuestDto build2 = GuestDto.builder()
                .boardId(board.get().getId())
                .memberId("teststt@nnn.com").build();
        //when
        ParticipateResult participateResult = guestService.joinRunning(build);
        guestService.joinRunning(build2);
        //then
        Assertions.assertThat(participateResult).isEqualTo(ParticipateResult.TRUE);
        Optional<Board> result = boardService.findByBoardId(board.get().getId());
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getJoinMember()).isEqualTo(2);
    }

    @Test
    public void findAllBoard(){
        BoardForm b1 = BoardForm.builder()
                .hostId("test1@naver.com")
                .totalMember(6)
                .title("mooyaho")
                .time(LocalDate.of(2023,8,17))
                .build();

        Optional<Board> board = boardService.openRunning(b1);

        BoardForm b2 = BoardForm.builder()
                .hostId("test1@naver.com")
                .totalMember(6)
                .title("mooyaho")
                .time(LocalDate.of(2023,7,8))
                .build();

        Optional<Board> board2 = boardService.openRunning(b2);

        BoardForm b3 = BoardForm.builder()
                .hostId("test2@naver.com")
                .totalMember(6)
                .title("mooyaho")
                .time(LocalDate.of(2023,7,8))
                .build();

        Optional<Board> board3 = boardService.openRunning(b3);


        Assertions.assertThat(board).isPresent();
        guestService.joinRunning(GuestDto.builder()
                        .memberId("test2@naver.com")
                        .boardId(board.get().getId())
                .build());

        guestService.joinRunning(GuestDto.builder()
                .memberId("test1@naver.com")
                .boardId(board3.get().getId())
                .build());

        List<Board> boards = boardService.findAllBoardByMemberId("test2@naver.com");
        Assertions.assertThat(boards.get(1).getHostId()).isEqualTo(board.get().getHostId());
        Assertions.assertThat(boards.size()).isEqualTo(2);
    }
}
