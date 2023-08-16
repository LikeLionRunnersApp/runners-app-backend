package likelion.running.domain.board;

import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.*;
import javax.persistence.*;


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
    private FlagType flag;

    private String place;

    private String time;
    private String runTime;
    private String walkTime;
    private int play_time;

    private int joinMember;
    private int totalMember;

    private BoardStatus status;
    public EditBoardDto.EditBoardDtoBuilder toEditor(){
        return EditBoardDto.builder()
                .title(title)
                .content(content)
                .place(place)
                .runTime(runTime)
                .walkTime(walkTime)
                .time(time)
                .totalMember(totalMember)
                .status(status)
                .flag(flag)
                .play_time(play_time);
    }

    public void edit(EditBoardDto editBoardDto){
        title = editBoardDto.getTitle();
        content = editBoardDto.getContent();
        flag = editBoardDto.getFlag();
        place = editBoardDto.getPlace();
        runTime = editBoardDto.getRunTime();
        walkTime = editBoardDto.getWalkTime();
        time = editBoardDto.getTime();
        totalMember = editBoardDto.getTotalMember();
        play_time = editBoardDto.getPlay_time();
        status = editBoardDto.getStatus();
    }
}
