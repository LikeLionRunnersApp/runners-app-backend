package likelion.running;

import likelion.running.domain.board.Board;
import likelion.running.domain.member.*;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.memberDto.GuestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;
    private final GuestService guestService;
    private final BoardService boardService;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityJpaRepository memberAuthorityJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    Set<MemberAuthority> set = new HashSet<>();
    @PostConstruct
    public void init(){

        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test", "010-0000-0000",passwordEncoder.encode("123456789"),true,set));

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
        guestService.joinRunning(GuestDto.builder()
                .memberId("test3@test.com")
                .boardId(board1.get().getId())
                .build());
        guestService.joinRunning(GuestDto.builder()
                .memberId("test4@test.com")
                .boardId(board1.get().getId())
                .build());

    }
}
