package likelion.running;

import likelion.running.domain.member.*;
import likelion.running.service.BoardService;
import likelion.running.service.GuestService;
import likelion.running.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberService memberService;
    private final GuestService guestService;
    private final BoardService boardService;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityJpaRepository memberAuthorityJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    Set<MemberAuthority> set = new HashSet<>();
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
    }
}
