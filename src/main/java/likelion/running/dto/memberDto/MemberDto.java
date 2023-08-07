package likelion.running.dto.memberDto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class MemberDto {

    private String memberId;

    private String passWord;

    private String checkPassWord;
    
    private String name;
}
