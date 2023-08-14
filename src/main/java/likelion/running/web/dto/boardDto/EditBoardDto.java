package likelion.running.web.dto.boardDto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@Builder
public class EditBoardDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String place;
    @NotBlank
    private String runningType;
    @NotBlank
    private String runTime;
    @NotBlank
    private String walkTime;
    @NotBlank
    private String time;

    private int totalMember;

    private int fullTime;

}
