package likelion.running.web.dto.boardDto;

import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.member.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BoardForm {

    private String hostId;
    private String title;
    private String content;
    private String place;

    private List<Member> guest;
    private FlagType flag;

    private LocalDate time;
    private String runTime;
    private String walkTime;
    private int play_time;
    private int totalMember;
    private BoardStatus status;
}
