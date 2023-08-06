package likelion.running.domain.member;

import lombok.Data;

@Data
public class Member {
    private Long id;
    private String memberId;
    private String name;
    private String password;
    private String email;
}
