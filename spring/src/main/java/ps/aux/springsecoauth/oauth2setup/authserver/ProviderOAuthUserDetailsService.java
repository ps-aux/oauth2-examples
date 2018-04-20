package ps.aux.springsecoauth.oauth2setup.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public class ProviderOAuthUserDetailsService implements UserDetailsService {

    @Autowired
    private OAuthUserDetailsService[] handlers;


    private String getClientName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User client = (User) auth.getPrincipal();
        return client.getUsername();
    }

    private OAuthUserDetailsService findHandler(String clientName) {
        for (OAuthUserDetailsService s : handlers)
            if (s.supports(clientName))
                return s;

        throw new IllegalStateException("Uknown OAuth client :" + clientName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findHandler(getClientName()).loadUserByUsername(username);
    }
}
