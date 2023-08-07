package likelion.running.domain.board;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@Entity
public class Board {

    @Id
    private Long id;
    private Long hostId;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
