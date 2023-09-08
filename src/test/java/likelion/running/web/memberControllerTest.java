package likelion.running.web;

import likelion.running.domain.board.Board;
import likelion.running.domain.guest.Guest;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.memberDto.GuestDto;
import likelion.running.web.dto.memberDto.MemberDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class memberControllerTest {
    @Autowired
    private GuestService guestService;
    @Autowired
    private BoardService boardService;

    @Test
    void signUp() {
    }

    @Test
    void members() {
    }

    @Test
    void participate() {
        //given
        BoardForm form = BoardForm.builder()
                .hostId("host@naver.com")
                .title("testRun")
                .content("let's run")
                .totalMember(6)
                .build();

        Optional<Board> board = boardService.openRunning(form);
        Assertions.assertThat(board).isPresent();

        //when
        GuestDto guest1 = GuestDto.builder()
                .boardId(board.get().getId())
                .memberId("test1@naver.com")
                .participate(true)
                .build();
        guestService.joinRunning(guest1);

        GuestDto guest2 = GuestDto.builder()
                .boardId(board.get().getId())
                .memberId("test2@naver.com")
                .participate(true)
                .build();
        guestService.joinRunning(guest2);
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId("host@naver.com");
        List<Guest> members = guestService.findMembers(memberDto);
        //then
        Assertions.assertThat(members.size()).isEqualTo(2);
    }
}