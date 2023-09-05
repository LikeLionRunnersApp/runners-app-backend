package likelion.running.service;

import likelion.running.domain.member.*;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.web.dto.memberDto.MemberDto;
import likelion.running.web.dto.memberDto.MemberEditDto;
import likelion.running.web.dto.memberDto.SignUpDto;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;//멤버 저장소
    private final MemberAuthorityJpaRepository memberAuthorityJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final String PHONE_PATTERN = "^(02|0[1-9][0-9]?)-[0-9]{3,4}-[0-9]{4}$";
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository, MemberAuthorityJpaRepository memberAuthorityJpaRepository, PasswordEncoder passwordEncoder) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberAuthorityJpaRepository = memberAuthorityJpaRepository;
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
        if(!isValidPhone(memberDto.getPhoneNum())){
            log.info(memberDto.getPhoneNum());
            return new SignUpResult("WrongNum");
        }

        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .phoneNum(memberDto.getPhoneNum())
                .password(passwordEncoder.encode(memberDto.getPassWord()))
                .authorities(new HashSet<>())
                .activated(true)
                .build();

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .member(member)
                .authority(authority)
                .build();

        member.getAuthorities().add(memberAuthority);

        log.info(member.getMemberId());
        memberAuthorityJpaRepository.save(memberAuthority);
        Member save = memberJpaRepository.save(member);
        log.info("멤버 저장 됨 {}",save.getId());
        log.info(save.getPassword());

        return new SignUpResult("true");
    }

    public Optional<Member> findById(Long id){
        return memberJpaRepository.findMemberById(id);
    }

    public Optional<Member> findByName(String name){
        return memberJpaRepository.findMemberByName(name);
    }
    public Optional<Member> findByMemberId(String memberId){
        return memberJpaRepository.findMemberByMemberId(memberId);
    }
    public Optional<Member> findByNameAndPhoneNum(MemberDto memberDto){
        return memberJpaRepository.findMemberByNameAndPhoneNum(memberDto.getName(),memberDto.getPhoneNum());
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhone(String phone){
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }

    @Transactional
    public boolean checkAuthCode(String memberId, String authCode){
        log.info("MemberId = {}",memberId);
        log.info("authCode = {}",authCode);
        Optional<Member> member = memberJpaRepository.findMemberByMemberId(memberId);
        log.info(String.valueOf(member.isEmpty()));
        return member.map(value -> value.getAuthCode().equals(authCode)).orElse(false);
    }

    @Transactional
    public boolean authCodeEdit(Member member, String message){

        Optional<Member> value = memberJpaRepository.findMemberByMemberId(member.getMemberId());

        if(value.isPresent()){
            MemberEditDto build = value.get().toEditor().AuthCode(message).build();
            value.get().edit(build);
            log.info("인증 코드 갱신 성공");
            return true;
        }
        log.info("인증 코드 갱신 실패");
        return false;
    }
}
