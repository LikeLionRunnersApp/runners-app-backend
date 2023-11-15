package likelion.running.web;


import likelion.running.domain.Result.KakaoResult;
import likelion.running.domain.member.Member;
import likelion.running.domain.token.KakaoToken;
import likelion.running.service.MemberService;
import likelion.running.service.TokenService;
import likelion.running.web.dto.memberDto.KakaoSignUpDto;
import likelion.running.web.dto.memberDto.LoginDto;
import likelion.running.web.dto.memberDto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KakaoController {
    private final MemberService memberService;
    private final TokenService tokenService;
    public KakaoController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }
    @PostMapping("/kakao/MemberCheck")
    public ResponseEntity<TokenDto> isOurMember(@RequestBody HashMap<String,String> accessToken) {
        log.info("{}",accessToken.get("accessToken"));
        KakaoResult kakaoResult = memberService.requestUser(accessToken.get("accessToken"));
        Optional<Member> member = memberService.findByMemberId(kakaoResult.getEmail());
        if(member.isEmpty()){
            return new ResponseEntity<>(new TokenDto(""), new HttpHeaders(), HttpStatus.OK);
        }
        LoginDto loginDto = new LoginDto();
        loginDto.setMemberId(member.get().getMemberId());
        return tokenService.makeTokenWithKakao(loginDto.getMemberId());
    }
    @PostMapping("/kakao/SignUp")
    public ResponseEntity<TokenDto> kakaoSingUp(@Validated @RequestBody KakaoSignUpDto signUpDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("전화번호 혹은 이름이 잘못입력되었습니다.");
        }
        return memberService.kakaoSignUp(signUpDto);
    }
}

