package likelion.running.web;

import likelion.running.domain.member.Member;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.service.LoginService;
import likelion.running.web.dto.memberDto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RestController
public class LoginController {

    LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public SignUpResult login(@Validated @RequestBody LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return new SignUpResult("emptyBox");
        }

        Optional<Member> member = loginService.login(loginDto);
        log.info("login {}",member);
        if(member.isEmpty()){
            return new SignUpResult("loginFail");
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,member);

        return new SignUpResult("true");
    }

    @PostMapping("/logout")
    public SignUpResult logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session!=null){
            session.invalidate();
        }
        return new SignUpResult("logout");
    }



}
