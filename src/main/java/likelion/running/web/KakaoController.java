package likelion.running.web;


import likelion.running.domain.Kakao.KakaoResult;
import likelion.running.domain.member.Member;
import likelion.running.domain.token.KakaoToken;
import likelion.running.service.MemberService;
import likelion.running.service.TokenService;
import likelion.running.web.dto.memberDto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
public class KakaoController {

    private final MemberService memberService;
    private final TokenService tokenService;
    public KakaoController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }


    @RequestMapping(value = "/kakaologin")
    public String loginPage(){
        return "kakaologin";
    }

    //redirect 경로 mapping
    @RequestMapping(value = "/kakaologin/redirect")
    public String kakaoLogin(@RequestParam(value = "code",required = false) String code){

        if(code!=null){//카카오측에서 보내준 code가 있다면 출력합니다
            System.out.println("code = " + code);

            //추가됨: 카카오 토큰 요청
            KakaoToken kakaoToken = memberService.requestToken(code);
            log.info("kakoToken = {}", kakaoToken);
            log.info(kakaoToken.getAccess_token());

            //추가됨: 유저정보 요청
            KakaoResult user = memberService.requestUser(kakaoToken.getAccess_token());
            log.info("user = {}",user);

        }
        return "redirectPage";
    }

    @PostMapping("/kakao/MemberCheck")
    public ResponseEntity<TokenDto> isOurMember(@RequestBody HashMap<String,String> accessToken){
        log.info("{}",accessToken.get("accessToken"));
        KakaoResult kakaoResult = memberService.requestUser(accessToken.get("accessToken"));
        Optional<Member> member = memberService.findByMemberId(kakaoResult.getEmail());

        if(member.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        LoginDto loginDto = new LoginDto();
        loginDto.setMemberId(member.get().getMemberId());
        return tokenService.makeTokenWithKakao(loginDto.getMemberId());
    }

    @PostMapping("/sign-up/2")
    public ResponseEntity<TokenDto> singUp2(@Validated @RequestBody KakaoSignUpDto signUpDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("전화번호 혹은 이름이 잘못입력되었습니다.");
        }
        return memberService.kakaoSignUp(signUpDto);
    }
}

