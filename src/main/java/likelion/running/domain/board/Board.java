package likelion.running.domain.board;

import likelion.running.domain.member.Member;
import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.*;
import javax.persistence.*;
import java.util.List;


@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostId;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String content;
    private String runningType;

    private String place;

    private String time;
    private String runTime;
    private String walkTime;
    private int fullTime;

    private int joinMember;
    private int totalMember;

    public EditBoardDto.EditBoardDtoBuilder toEditor(){
        return EditBoardDto.builder()
                .title(title)
                .content(content)
                .runningType(runningType)
                .place(place)
                .runTime(runTime)
                .walkTime(walkTime)
                .time(time)
                .totalMember(totalMember)
                .fullTime(fullTime);
    }

    public void edit(EditBoardDto editBoardDto){
        title = editBoardDto.getTitle();
        content = editBoardDto.getContent();
        runningType = editBoardDto.getRunningType();
        place = editBoardDto.getPlace();
        runTime = editBoardDto.getRunTime();
        walkTime = editBoardDto.getWalkTime();
        time = editBoardDto.getTime();
        totalMember = editBoardDto.getTotalMember();
        fullTime = editBoardDto.getFullTime();
    }
}
