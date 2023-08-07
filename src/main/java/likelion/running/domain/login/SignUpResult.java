package likelion.running.domain.login;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpResult {
    private String signUpResult;

    public SignUpResult(String signUpResult) {
        this.signUpResult = signUpResult;
    }
}
