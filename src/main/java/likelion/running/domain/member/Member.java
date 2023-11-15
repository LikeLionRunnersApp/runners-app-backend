package likelion.running.domain.member;

import likelion.running.web.dto.memberDto.MemberEditDto;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    @Column(length = 30, name = "memberId")
    private String memberId;

    @Column(length = 10, name = "name")
    private String name;

    @Column(length = 13, name = "phoneNum")
    private String phoneNum;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(mappedBy = "member")
    private Set<MemberAuthority> authorities = new HashSet<>();

    @Column(name = "authCode")
    private String authCode;

    public void edit(MemberEditDto memberEditDto) {
        this.authCode = memberEditDto.getAuthCode();
        this.name = memberEditDto.getName();
        this.password = memberEditDto.getPassword();
        this.activated = memberEditDto.isActivated();
        this.phoneNum = memberEditDto.getPhoneNum();
        this.authorities = memberEditDto.getAuthorities();
    }
    public MemberEditDto.MemberEditDtoBuilder toEditor() {
        return MemberEditDto.builder()
                .memberId(memberId)
                .name(name)
                .activated(activated)
                .password(password)
                .phoneNum(phoneNum)
                .AuthCode(authCode);
    }
}
