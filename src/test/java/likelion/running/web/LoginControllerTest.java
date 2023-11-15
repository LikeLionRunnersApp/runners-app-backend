package likelion.running.web;

import likelion.running.domain.member.Member;
import likelion.running.service.LoginService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.BoolRespose;
import likelion.running.web.dto.memberDto.SignUpDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    LoginService loginService;
    @Test
    public void signUp(){
        //given
        SignUpDto member = SignUpDto.builder().build();
        member.setMemberId("lion@naver.com");
        member.setPassword("qwer");
        member.setCheckPassWord("qwer");
        member.setName("park");
        //when
        BoolRespose result = memberService.signUp(member);

        //then
        Assertions.assertThat(result.isOk()).isEqualTo(true);
        Optional<Member> find = memberService.findByMemberId("lion@naver.com");
        Assertions.assertThat(find.get().getName()).isEqualTo("park");

    }

    @Test
    public void login(){
        //given
        SignUpDto member = SignUpDto.builder()
                .build();
        member.setMemberId("lion@naver.com");
        member.setPassword("qwer");
        member.setCheckPassWord("qwer");
        member.setName("park");
        BoolRespose result = memberService.signUp(member);
        //when
        UserDetails userDetails = loginService.loadUserByUsername("lion@naver.com");
        //then
        Assertions.assertThat(userDetails.getUsername()).isEqualTo("lion@naver.com");

    }

}