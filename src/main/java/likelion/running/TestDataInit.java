package likelion.running;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
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
    @PostConstruct
    public void init(){
        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test", "12345"));
        Board board = boardJpaRepository.save(new Board(1L,"tset@test.com","ttt","contentttt","work","daejeon","10:30","1","2",20,5,5));
        log.info("test {}",test);
        log.info("test board {}",board);
    }
}
