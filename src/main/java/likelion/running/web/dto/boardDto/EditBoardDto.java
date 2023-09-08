package likelion.running.web.dto.boardDto;

import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
public class EditBoardDto {

    private String memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String place;

    private FlagType flag;
    @NotBlank
    private String runTime;
    @NotBlank
    private String walkTime;

    private LocalDate time;

    private int repeat;

    private int totalMember;

    private int play_time;

    private BoardStatus status;
}
