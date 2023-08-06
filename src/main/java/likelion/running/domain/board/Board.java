package likelion.running.domain.board;

import lombok.Data;

@Data
public class Board {

    private Long id;
    private Long hostId;

    private String title;
    private String content;
    private String runningType;

    private String place;

    private String time;
    private String runTime;
    private String walkTime;
    private int fullTime;
}
