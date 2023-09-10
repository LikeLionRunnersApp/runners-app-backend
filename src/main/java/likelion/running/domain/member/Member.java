package likelion.running.domain.member;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@Entity
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

//    @ManyToMany
//    @JoinTable(
//            name = "user_authority",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
//    )
//    private Set<Authority> authorities;

    @OneToMany(mappedBy = "member")
    private Set<MemberAuthority> authorities = new HashSet<>();
}
