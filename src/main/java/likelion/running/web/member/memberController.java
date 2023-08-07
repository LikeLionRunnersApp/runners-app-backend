package likelion.running.web.member;

import likelion.running.domain.login.SignUpResult;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberRepository;
import likelion.running.dto.memberDto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@RestController
public class memberController {

    static final MemberRepository memberRepository = MemberRepository.getInstance();
    @PostMapping("/sign-up")
    public SignUpResult singUp(@Validated @RequestBody MemberDto memberDto){
        Member member = new Member();
        SignUpResult result = new SignUpResult("true");

        if(memberRepository.findByName(memberDto.getMemberId()).isPresent()){
            result.setSignUpResult("duplicatedId");
            return result;
        }
        if(!isValidEmail(memberDto.getMemberId())){
            result.setSignUpResult("idValidation");
            return result;
        }
        if(!memberDto.getPassWord().equals(memberDto.getCheckPassWord())){
            result.setSignUpResult("pwValidation");
            return result;
        }

        member.setMemberId(memberDto.getMemberId());//사용자 지정 아이디
        member.setName(memberDto.getName());//사용자 이름
        member.setPassword(memberDto.getPassWord());//사용자 암호
        memberRepository.save(member);
        log.info("회원가입 성공 {}",member.getMemberId());
        return result;
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
