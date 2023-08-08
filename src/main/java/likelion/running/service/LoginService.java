package likelion.running.service;

import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.web.dto.memberDto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Optional;

@Slf4j
@Service
public class LoginService {

    private final MemberJpaRepository memberJpaRepository;

    public LoginService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public Optional<Member> login(@Validated @RequestBody LoginDto loginDto){
        return memberJpaRepository.findMemberByMemberId(loginDto.getMemberId())
                .filter(m -> m.getPassword().equals(loginDto.getPassword()));
    }
}
