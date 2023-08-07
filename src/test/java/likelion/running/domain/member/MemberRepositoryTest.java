package likelion.running.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;


class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();
    @Test
    public void save(){
        //given
        Member member = new Member();
        member.setName("ya ong");
        member.setMemberId("testMan");
        member.setPassword("123");
        //when
        Member result = memberRepository.save(member);

        //then
        Assertions.assertThat(result).isEqualTo(member);
        Assertions.assertThat(result.getName()).isEqualTo(member.getName());
    }

    @Test
    public void findById(){
        //given
        Member member1 = new Member();
        member1.setName("ya ong");
        member1.setMemberId("testMan");
        member1.setPassword("123");

        Member member2 = new Member();
        member2.setName("ya ong2");
        member2.setMemberId("testMan2");
        member2.setPassword("1234");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        Optional<Member> result = memberRepository.findByName(member2.getName());

        //then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get()).isEqualTo(member1);
        Assertions.assertThat(result.get()).isEqualTo(member2);
        Assertions.assertThat(result.get().getName()).isEqualTo(member2.getName());
    }

}