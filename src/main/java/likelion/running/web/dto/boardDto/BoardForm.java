package likelion.running.web.dto.boardDto;

import likelion.running.domain.member.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardForm {

    private String hostId;
    private String title;
    private String content;
    private String place;

    private List<Member> guest;
    private String runningType;

    private String runTime;
    private String walkTime;
    private int fullTime;
    private int totalMember;
}
