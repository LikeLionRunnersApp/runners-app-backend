package likelion.running.domain.member;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 30)
    private String memberId;

    @Column(length = 10)
    private String name;
    private String password;
}
