package likelion.running.web;

import likelion.running.domain.guest.Guest;
import likelion.running.domain.member.Member;
import likelion.running.domain.participate.ParticipateResult;
import likelion.running.service.GuestService;
import likelion.running.service.MemberService;
import likelion.running.service.TokenService;
import likelion.running.web.dto.BoolRespose;
import likelion.running.web.dto.memberDto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private final MemberService memberService;
    private final GuestService guestService;
    private final TokenService tokenService;
    @Autowired
    public MemberController(MemberService memberService, GuestService guestService, TokenService tokenService) {
        this.memberService = memberService;
        this.guestService = guestService;
        this.tokenService = tokenService;
    }
    @PostMapping("/sign-up")
    public BoolRespose signUp(@Validated @RequestBody SignUpDto signUpDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("아이디 혹은 비밀번호를 잘못입력했습니다.");
        }
        return memberService.signUp(signUpDto);
    }   // 회원가입

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        return tokenService.makeToken(loginDto);
    } // 회원 로그인

    @GetMapping("/members")
    public List<Guest> members(@RequestBody MemberDto memberDto) {
        return guestService.findMembers(memberDto);
    }   // 회원 명단

    @PostMapping("/participate")
    public ParticipateResult participate(@RequestBody GuestDto guestDto) {
        return guestService.joinRunning(guestDto);
    }   // 모임 참가

    @PostMapping("/checkDuplicateMemberId")
    public BoolRespose checkDuplicate(@RequestBody HashMap<String,String> member) {
        BoolRespose build;
        String memberId = member.get("memberId");
        Optional<Member> byMemberId = memberService.findByMemberId(memberId);

        log.info(memberId);

        if(byMemberId.isEmpty()){
            build = BoolRespose.builder()
                    .ok(true)
                    .build();
        } else {
            build = BoolRespose.builder()
                    .ok(false)
                    .build();
        }
        return build;
    }   // 회원 아이디 중복 검사
}
