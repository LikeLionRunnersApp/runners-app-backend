package likelion.running;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.member.*;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.memberDto.AuthorityDto;
import likelion.running.web.dto.memberDto.GuestDto;
import likelion.running.web.dto.memberDto.SignUpDto;
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
    private final MemberService memberService;
    private final GuestService guestService;
    private final BoardService boardService;
    private final AuthorityJpaRepository authorityJpaRepository;
    @PostConstruct
    public void init(){
        Authority authority1 = Authority.builder()
                .authorityName("ROLE_USER").build();
        Authority authority2 = Authority.builder()
                .authorityName("ROLE_ADMIN").build();
        if(authorityJpaRepository.findByAuthorityName(authority1.getAuthorityName())==null){
            authorityJpaRepository.save(authority1);
        }
        if(authorityJpaRepository.findByAuthorityName(authority2.getAuthorityName())==null){
            authorityJpaRepository.save(authority2);
        }
        Set<AuthorityDto> set = new HashSet<>();
        set.add(AuthorityDto.builder()
                .authorityName("ROLE_ADMIN")
                .build());

        set.add(AuthorityDto.builder()
                .authorityName("ROLE_USER")
                .build());

        if(memberService.findByMemberId("admin@running.com").isEmpty()){
            memberService.signUp(SignUpDto.builder()
                            .memberId("admin@running.com")
                            .name("admin")
                            .password("runningadmin")
                            .checkPassWord("runningadmin")
                            .phoneNum("010-0000-0000")
                            .authorityDtoSet(set)
                        .build());
            }

        if(boardService.findAllBoardByMemberId("admin@running.com").isEmpty()){
            boardService.openRunning(
                    BoardForm.builder()
                        .title("test")
                        .hostId("admin@running.com")
                        .content("test cotent")
                        .flag(FlagType.RUN)
                        .time(LocalDate.of(2024,7,15))
                        .totalMember(6)
                        .repeat(3)
                        .place("나메크성")
                        .build()
                );

            boardService.openRunning(
                    BoardForm.builder()
                            .title("밥은 먹고 뛰냐")
                            .hostId("admin@running.com")
                            .content("test content")
                            .flag(FlagType.INTERVAL)
                            .time(LocalDate.of(2024,11,23))
                            .totalMember(9)
                            .build()
            );

            Optional<Board> board = boardService.openRunning(
                    BoardForm.builder()
                            .title("board")
                            .hostId("test@running.com")
                            .content("test test content")
                            .flag(FlagType.WALK)
                            .time(LocalDate.of(2024,1,1))
                            .totalMember(10)
                            .repeat(4)
                            .place("장대동")
                            .status(BoardStatus.START)
                            .build()
            );

            guestService.joinRunning(GuestDto.builder()
                            .memberId("admin@running.com")
                            .boardId(board.get().getId())
                            .participate(true)
                    .build());
        }
    }
}

        // 예시로 권한 이름을 설정
        // 다른 필드 값 설정
        // Authority 저장

//        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test","010-0000-0000", passwordEncoder.encode("123456789"),true,set,""));
//        BoolRespose test = memberService.signUp(SignUpDto.builder()
//                .memberId("test@test.com")
//                .phoneNum("010-0000-0000")
//                .checkPassWord("123456789")
//                .password("123456789")
//                .name("test")
//                .build());
//
//        memberAuthorityJpaRepository.save(MemberAuthority.builder()
//                        .id(1L)
//                        .member(memberService.findByMemberId("test@test.com").get())
//                        .authority(Authority.builder()
//                                .authorityName("ROLE_ADMIN")
//                                .build()
//                        ).build()
//                );
//
//        Optional<Board> board1 = boardService.openRunning(BoardForm.builder()
//                        .hostId("test@test.com")
//                        .title("tttt")
//                        .totalMember(10)
//                        .time(LocalDate.of(2023,8,17))
//                .build());
//
//        Optional<Board> board2 = boardService.openRunning(BoardForm.builder()
//                .hostId("test1@test.com")
//                .title("tttt")
//                .totalMember(10)
//                .time(LocalDate.of(2023,9,16))
//                .build());
//
//        Optional<Board> board3 = boardService.openRunning(BoardForm.builder()
//                .hostId("test@test.com")
//                .title("tttt")
//                .totalMember(10)
//                .time(LocalDate.of(2023,10,15))
//                .build());
//
//        Optional<Board> board4 = boardService.openRunning(BoardForm.builder()
//                .hostId("test@test.com")
//                .title("tttt")
//                .totalMember(10)
//                .time(LocalDate.of(2023,9,15))
//                .build());
////        log.info("test {}",test);
//        log.info("test board {}",board1);
//
//        guestService.joinRunning(GuestDto.builder()
//                .memberId("test@test.com")
//                .participate(true)
//                .boardId(board2.get().getId())
//                .build());
//
//        guestService.joinRunning(GuestDto.builder()
//                .memberId("test2@test.com")
//                .participate(true)
//                .boardId(board1.get().getId())
//                .build());
//        guestService.joinRunning(GuestDto.builder()
//                .memberId("test3@test.com")
//                .participate(true)
//                .boardId(board1.get().getId())
//                .build());
//        guestService.joinRunning(GuestDto.builder()
//                .memberId("test4@test.com")
//                .participate(true)
//                .boardId(board1.get().getId())
//                .build());
