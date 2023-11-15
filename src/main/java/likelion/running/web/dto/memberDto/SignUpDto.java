package likelion.running.web.dto.memberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    @NotEmpty
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String memberId;
    @NotEmpty
    @Size(max = 13)
    private String phoneNum;
    @NotEmpty
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    @NotEmpty
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String checkPassWord;
    @NotEmpty
    @Size(max = 10)
    private String name;
    private Set<AuthorityDto> authorityDtoSet;
}
