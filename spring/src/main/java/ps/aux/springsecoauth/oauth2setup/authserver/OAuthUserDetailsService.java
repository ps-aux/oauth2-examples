package ps.aux.springsecoauth.oauth2setup.authserver;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface OAuthUserDetailsService extends UserDetailsService {

    boolean supports(String clientName);
}
