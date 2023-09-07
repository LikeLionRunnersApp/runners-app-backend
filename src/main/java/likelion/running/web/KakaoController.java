package likelion.running.web;



import likelion.running.domain.Kakao.KakaoResult;
import likelion.running.domain.token.KakaoToken;
import likelion.running.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@Controller
public class KakaoController {
    private final LoginService loginService;

    public KakaoController(LoginService loginService) {
        this.loginService = loginService;
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

