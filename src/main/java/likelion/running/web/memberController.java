package likelion.running.web;

import likelion.running.domain.member.Member;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.service.MemberService;
import likelion.running.web.dto.memberDto.MemberDto;
import likelion.running.web.dto.memberDto.SignUpDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class memberController {

    private final MemberService memberService;
    @Autowired
    public memberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/sign-up")
    public SignUpResult signUp(@Validated @RequestBody SignUpDto signUpDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            //검증 부분 설정 필요
            log.info("아이디 혹은 비밀번호를 잘못입력했습니다.");
        }
        return memberService.save(signUpDto);
    }

    @GetMapping("/members")
    public List<Member> members(@RequestBody MemberDto memberDto){
        //json 배열 반환


        return null;
    }
}
