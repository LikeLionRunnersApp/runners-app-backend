package likelion.running.web;

import likelion.running.domain.member.Member;
import likelion.running.service.EmailService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.BoolRespose;
import likelion.running.web.dto.memberDto.AuthDto;
import likelion.running.web.dto.memberDto.LoginDto;
import likelion.running.web.dto.memberDto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberEditController {
    private final MemberService memberService;
    private final EmailService emailService;

    @Autowired
    public MemberEditController(MemberService memberService, EmailService emailService) {
        this.memberService = memberService;
        this.emailService = emailService;
    }

    @PostMapping("/findMemberId")
    public String handleFindMemberName(@RequestBody MemberDto memberDto) {
        // 아이디 찾기 로직 수행 후 결과를 모델에 추가하고 결과 화면 템플릿을 반환...log.info(name.get());
        Optional<Member> member = memberService.findByNameAndPhoneNum(memberDto);

        log.info(String.valueOf(member.isEmpty()));

        return member.map(Member::getMemberId).orElse("");
    }
    // memberId : ""

    @PostMapping("/auth-send")
    public BoolRespose sendAuthMail(@RequestBody MemberDto memberDto) throws Exception {
        // 비밀번호 재설정 로직 수행 후 결과를 모델에 추가하고 결과 화면 템플릿을 반환...
        Optional<Member> member = memberService.findByNameAndPhoneNum(memberDto);
        if(member.isPresent()){

            String memberId = member.map(Member::getMemberId).orElseThrow();
            String phoneNum = member.map(Member::getPhoneNum).orElseThrow();

            log.info(phoneNum);
            log.info(memberId);

            String message = emailService.sendSimpleMessage(memberId);

            return memberService.authCodeEdit(member.get(),message);
        }

        return BoolRespose.builder()
                .ok(false)
                .build();
    }//이메일, 전화번호, 이름을 통해서 인증코드 보내기, AuthCode member에 저장

    @PostMapping("/auth-check")
    public BoolRespose checkAuthCode(@RequestBody AuthDto authDto){
        return memberService.checkAuthCode(authDto.getMemberId(), authDto.getAuthCode());
    }
    // 인증코드 인증 부분 구현해야함

    @PatchMapping("/resetPassword")
    public BoolRespose resetPassword(@RequestBody LoginDto loginDto){
        return memberService.passwordReset(loginDto);
    }

}
