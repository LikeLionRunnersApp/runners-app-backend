package likelion.running.web.dto.memberDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotEmpty
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    String memberId;

    @NotEmpty
    @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
    String password;

}
