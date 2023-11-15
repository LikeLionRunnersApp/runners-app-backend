package likelion.running.domain.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BoardStatus {
    START("start"),PARTICIPATION("participation"),COMPLETE("complete");

    private final String value;

    BoardStatus(String value) {
        this.value = value;
    }
    @JsonCreator
    public static BoardStatus from(String value) {
        for (BoardStatus status : BoardStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue(){
        return value;
    }
}
