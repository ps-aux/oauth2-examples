package ps.aux.springsecoauth.oauth2setup.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ps.aux.springsecoauth.oauth2setup.authserver.OAuthUserDetailsService;

import static java.util.Collections.singletonList;

@Service
public class FooUserDetailsService implements OAuthUserDetailsService {

    private final String password;

    @Autowired
    public FooUserDetailsService(PasswordEncoder encoder) {
        password = encoder.encode("password");
    }

    @Override
    public boolean supports(String clientName) {
        return clientName.equals("foo-client");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("john.doe@example.com",
                password,
                singletonList((new SimpleGrantedAuthority("FOO"))));
    }
}
