package likelion.running.domain.guest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import likelion.running.domain.board.Board;
import lombok.*;
import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    private String guestId;

    private boolean participate;
}
