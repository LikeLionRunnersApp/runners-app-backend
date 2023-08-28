package likelion.running.service;

import likelion.running.domain.member.Authority;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.web.dto.memberDto.SignUpDto;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;//멤버 저장소
    private final PasswordEncoder passwordEncoder;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository, PasswordEncoder passwordEncoder) {
        this.memberJpaRepository = memberJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SignUpResult signUp(SignUpDto memberDto){
        if(!isValidEmail(memberDto.getMemberId())){
            return new SignUpResult("idValidation");
        }
        if(memberJpaRepository.findOneWithAuthoritiesByMemberId(memberDto.getMemberId()).orElse(null)!=null){
            return new SignUpResult("duplicatedId");
        }
        if(!memberDto.getPassWord().equals(memberDto.getCheckPassWord())){
            return new SignUpResult("pwValidation");
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .password(passwordEncoder.encode(memberDto.getPassWord()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
        log.info(member.getMemberId());
        Member save = memberJpaRepository.save(member);
        log.info("멤버 저장 됨 {}",save.getId());
        log.info(save.getPassword());
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
