package likelion.running.domain.participate;

import lombok.Getter;

@Getter
public enum ParticipateResult {
    FULL("full"),TRUE("true"),FALSE("false");

    private final String value;

    ParticipateResult(String value) {
        this.value = value;
    }
}
