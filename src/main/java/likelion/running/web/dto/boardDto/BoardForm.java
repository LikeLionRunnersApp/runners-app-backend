package likelion.running.web.dto.boardDto;

import likelion.running.domain.member.Member;
import lombok.Data;

import java.util.List;

@Data
public class BoardForm {

    private String hostId;
    private String title;
    private String content;

    private List<Member> guest;
    private int totalMember;
    private String runningType;

    private String runTime;
    private String walkTime;
    private int fullTime;
}
