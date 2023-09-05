package likelion.running.web.dto.memberDto;

import likelion.running.domain.member.MemberAuthority;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MemberEditDto {
    String memberId;
    String password;
    String AuthCode;
    String name;
    String phoneNum;
    boolean activated;
    Set<MemberAuthority> authorities;
}
