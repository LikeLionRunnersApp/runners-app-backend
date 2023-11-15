package likelion.running.web.dto.memberDto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class KakaoSignUpDto {
    @NotEmpty
    @Size(max = 10)
    private String name;
    @NotEmpty
    @Size(max = 13)
    private String phoneNum;
    @NotEmpty
    private String accessToken;
}
