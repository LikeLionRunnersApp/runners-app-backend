package likelion.running.web;


import likelion.running.domain.Kakao.KakaoResult;
import likelion.running.domain.member.Authority;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberAuthority;
import likelion.running.domain.token.KakaoToken;
import likelion.running.domain.token.TokenProvider;
import likelion.running.filter.JwtFilter;
import likelion.running.service.LoginService;
import likelion.running.service.MemberService;
import likelion.running.web.dto.memberDto.KakaoSignUpDto;
import likelion.running.web.dto.memberDto.LoginDto;
import likelion.running.web.dto.memberDto.SignUpDto;
import likelion.running.web.dto.memberDto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Controller
public class KakaoController {
    private final LoginService loginService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;

    public KakaoController(LoginService loginService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, MemberService memberService) {

        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.memberService = memberService;
    }


    @RequestMapping(value = "/kakaologin")
    public String loginPage(){
        return "kakaologin";
    }

    //redirect 경로 mapping
    @RequestMapping(value = "/login/kakao-redirect")
    public String kakaoLogin(@RequestParam(value = "code",required = false) String code){

        if(code!=null){//카카오측에서 보내준 code가 있다면 출력합니다
            System.out.println("code = " + code);

            //추가됨: 카카오 토큰 요청
            KakaoToken kakaoToken = loginService.requestToken(code);
            log.info("kakoToken = {}", kakaoToken);
            log.info(kakaoToken.getAccess_token());

            //추가됨: 유저정보 요청
            KakaoResult user = loginService.requestUser(kakaoToken.getAccess_token());
            log.info("user = {}",user);

        }
        return "redirectPage";
    }


    @ResponseBody
    @PostMapping("/login/kakao-redirect/signup")
    public ResponseEntity<TokenDto> kakaoSignUp(@RequestBody KakaoSignUpDto kakaoSignUpDto){

        //카카오측에서 보내준 code가 있다면 출력합니다
            //추가됨: 유저정보 요청
            KakaoResult user = loginService.requestUser(kakaoSignUpDto.getAccess_token());

            Optional<Member> member = memberService.findByMemberId(user.getEmail());

            if(member.isEmpty()){
                memberService.kakaoSignUp(user,kakaoSignUpDto);
                log.info("회원가입 성공");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(),null);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.info("authentication = {}",authentication.getName());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(SecurityContextHolder.getContext().getAuthentication().getName());
            String jwt = tokenProvider.createToken(authentication);

            log.info("jwt = {}",jwt);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer " + jwt);

            return new ResponseEntity<>(new TokenDto(jwt),httpHeaders, HttpStatus.OK);
    }
}

