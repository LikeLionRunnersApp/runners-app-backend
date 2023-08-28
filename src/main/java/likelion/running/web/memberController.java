package likelion.running.web;

import likelion.running.domain.guest.Guest;
import likelion.running.domain.participate.ParticipateResult;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.service.GuestService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.memberDto.GuestDto;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class memberController {

    private final MemberService memberService;
    private final GuestService guestService;

    @Autowired
    public memberController(MemberService memberService, GuestService guestService) {
        this.memberService = memberService;
        this.guestService = guestService;
    }

    @PostMapping("/sign-up")
    public SignUpResult signUp(@Validated @RequestBody SignUpDto signUpDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            //검증 부분 설정 필요
            log.info("아이디 혹은 비밀번호를 잘못입력했습니다.");
        }
        return memberService.signUp(signUpDto);
    }

    @GetMapping("/members")
    public List<Guest> members(@RequestBody MemberDto memberDto){
        //json 배열 반환
        return guestService.findMembers(memberDto);
    }

    @PostMapping("/participate")
    public ParticipateResult participate(@RequestBody GuestDto guestDto){
        return guestService.joinRunning(guestDto);
    }

}
