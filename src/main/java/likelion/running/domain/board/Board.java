package likelion.running.domain.board;

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
    private String runningType;

    private String place;

    private String time;
    private String runTime;
    private String walkTime;
    private int fullTime;

    private int joinMember;
    private int totalMember;

}
