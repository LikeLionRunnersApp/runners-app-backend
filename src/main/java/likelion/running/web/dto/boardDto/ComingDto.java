package likelion.running.web.dto.boardDto;

import likelion.running.domain.board.FlagType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ComingDto {
    String title;
    FlagType flag;
    LocalDate time;
}
