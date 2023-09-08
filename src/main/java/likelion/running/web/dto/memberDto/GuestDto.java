package likelion.running.web.dto.memberDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestDto {

    String memberId;
    Long boardId;
    boolean participate;
}
