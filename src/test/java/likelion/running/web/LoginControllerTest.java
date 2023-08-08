package likelion.running.web;

import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.service.LoginService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.memberDto.LoginDto;
import likelion.running.web.dto.memberDto.MemberDto;
import likelion.running.web.dto.memberDto.SignUpDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        SignUpDto member = new SignUpDto();
        member.setMemberId("lion@naver.com");
        member.setPassWord("qwer");
        member.setCheckPassWord("qwer");
        member.setName("park");
        //when
        SignUpResult result = memberService.save(member);

        //then
        Assertions.assertThat(result.getSignUpResult()).isEqualTo("true");

    }

    @Test
    public void login(){
        //given
        SignUpDto member = new SignUpDto();
        member.setMemberId("lion@naver.com");
        member.setPassWord("qwer");
        member.setCheckPassWord("qwer");
        member.setName("park");
        SignUpResult result = memberService.save(member);
        //when
        LoginDto login = new LoginDto();
        login.setMemberId("lion@naver.com");
        login.setPassword("qwer");
        Optional<Member> result2 = loginService.login(login);

        //then
        Assertions.assertThat(result2).isPresent();
        Assertions.assertThat(result2.get().getPassword()).isEqualTo("qwer");
//        Assertions.assertThat(result2).isEmpty();

    }

}