package likelion.running.domain.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FlagType {
    RUN("run"),WALK("walk"),INTERVAL("interval");

    private final String value;

    FlagType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue(){return value;}

    @JsonCreator
    public static FlagType from(String value){
        for (FlagType status : FlagType.values()) {
            if(status.getValue().equals(value)){
                return status;
            }
        }
        return null;
    }
}
