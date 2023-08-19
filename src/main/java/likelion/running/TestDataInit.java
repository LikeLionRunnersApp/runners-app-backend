package likelion.running;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.memberDto.GuestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;
    private final GuestService guestService;
    private final BoardService boardService;
    @PostConstruct
    public void init(){
        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test", "12345"));
        Optional<Board> board1 = boardService.openRunning(BoardForm.builder()
                        .hostId("test@test.com")
                        .title("tttt")
                        .totalMember(10)
                        .time(LocalDate.of(2023,8,17))
                .build());
        Optional<Board> board2 = boardService.openRunning(BoardForm.builder()
                .hostId("test@test.com")
                .title("tttt")
                .totalMember(10)
                .time(LocalDate.of(2023,8,16))
                .build());
        Optional<Board> board3 = boardService.openRunning(BoardForm.builder()
                .hostId("test@test.com")
                .title("tttt")
                .totalMember(10)
                .time(LocalDate.of(2023,8,15))
                .build());

        log.info("test {}",test);
        log.info("test board {}",board1);
        guestService.joinRunning(GuestDto.builder()
                .memberId("test2@test.com")
                .boardId(board1.get().getId())
                .build());
    }
}
