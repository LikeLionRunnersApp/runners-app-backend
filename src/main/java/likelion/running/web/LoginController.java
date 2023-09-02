package likelion.running.web;

import likelion.running.SessionConst;
import likelion.running.domain.member.Member;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.domain.token.TokenProvider;
import likelion.running.filter.JwtFilter;
import likelion.running.service.LoginService;
import likelion.running.web.dto.memberDto.LoginDto;
import likelion.running.web.dto.memberDto.TokenDto;
import likelion.running.domain.Kakao.KakaoResult;
import likelion.running.domain.token.KakaoToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
public class LoginController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final LoginService loginService;

    @Autowired
    public LoginController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder
    ,LoginService loginService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getMemberId(),loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication = {}",authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt),httpHeaders, HttpStatus.OK);
    }

    @RequestMapping("/kakaologin")
    public String loginPage(){
        return "login";
    }

    //redirect 경로 mapping
    @RequestMapping("/login/kakao-redirect")
    public String kakaoLogin(@RequestParam(value = "code",required = false) String code){
        if(code!=null){//카카오측에서 보내준 code가 있다면 출력합니다
            System.out.println("code = " + code);

            //추가됨: 카카오 토큰 요청
            KakaoToken kakaoToken = loginService.requestToken(code);
            log.info("kakoToken = {}", kakaoToken);

            //추가됨: 유저정보 요청
            KakaoResult user = loginService.requestUser(kakaoToken.getAccess_token());
            log.info("user = {}",user);
        }
        return "redirectPage"; //만들어둔 응답받을 View 페이지 redirectPage.html 리턴
    }

}
