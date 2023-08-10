package likelion.running;

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

    @PostConstruct
    public void init(){
        Member test = memberJpaRepository.save(new Member(1L, "test@test.com", "test", "12345"));
        log.info("test {}",test);
    }
}
