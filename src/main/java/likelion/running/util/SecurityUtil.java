package likelion.running.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    private SecurityUtil(){
    }
    public static Optional<String> getCurrentUsername(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String memberId = null;
        if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails springSecurityMember = (UserDetails) authentication.getPrincipal();
            memberId = springSecurityMember.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            memberId = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(memberId);
    }
}
