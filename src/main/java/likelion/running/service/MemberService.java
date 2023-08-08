package likelion.running.service;

import likelion.running.domain.login.SignUpResult;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.web.dto.memberDto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;//멤버 저장소

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public SignUpResult save(MemberDto memberDto){
        if(memberJpaRepository.findMemberByMemberId(memberDto.getMemberId()).isEmpty()){
            return new SignUpResult("duplicatedId");
        }
        if(!isValidEmail(memberDto.getMemberId())){
            return new SignUpResult("idValidation");
        }
        if(memberDto.getPassWord().equals(memberDto.getCheckPassWord())){
            return new SignUpResult("pwValidation");
        }

        Optional<Member> member = Optional.of(memberJpaRepository.save(Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .password(memberDto.getPassWord())
                .build()));
        log.info("멤버 저장 됨 {}",member.get().getId());

        return new SignUpResult("true");
    }

    public Optional<Member> findById(Long id){
        return memberJpaRepository.findMemberById(id);
    }

    public Optional<Member> findByName(String name){
        return memberJpaRepository.findMemberByMemberId(name);
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
