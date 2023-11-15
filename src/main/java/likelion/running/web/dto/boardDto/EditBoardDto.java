package likelion.running.web.dto.boardDto;

import likelion.running.domain.board.BoardStatus;
import likelion.running.domain.board.FlagType;
import likelion.running.domain.guest.Guest;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class EditBoardDto {
    private String memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private FlagType flag;
    @NotBlank
    private String place;
    private LocalDate time;
    @NotBlank
    private String runTime;
    @NotBlank
    private String walkTime;
    private int playTime;
    private int repeat;
    private int totalMember;
    private BoardStatus status;
    private List<Guest> guests;
}
