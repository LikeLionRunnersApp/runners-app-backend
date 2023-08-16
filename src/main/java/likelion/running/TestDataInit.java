package likelion.running;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.service.GuestService;
import likelion.running.web.dto.memberDto.GuestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final GuestService guestService;
    @PostConstruct
    public void init(){
        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test", "12345"));
        Board board = boardJpaRepository.save(new Board(1L,"test@test.com","ttt","contentttt", FlagType.WALK,"daejeon","10:30","1","2",20,5,5, BoardStatus.START));
        log.info("test {}",test.toString());
        log.info("test board {}",board.toString());
        GuestDto build1 = GuestDto.builder().boardId(board.getId()).participate(true).memberId("ttt@tttt.com").build();
        GuestDto build2 = GuestDto.builder().boardId(board.getId()).participate(true).memberId("ttt2@tttt.com").build();
        GuestDto build3 = GuestDto.builder().boardId(board.getId()).participate(true).memberId("ttt3@tttt.com").build();

        guestService.joinRunning(build1);
        guestService.joinRunning(build2);
        guestService.joinRunning(build3);
    }
}
