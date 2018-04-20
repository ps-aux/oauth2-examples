package ps.aux.springsecoauth.oauth2setup.authserver;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class OauthAuthenticationManager {

    public static AuthenticationManager createAuthenticationManager(UserDetailsService userDetailService,
                                                                    PasswordEncoder encoder) {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authProvider(userDetailService, encoder));

        return new ProviderManager(providers);
    }

    private static DaoAuthenticationProvider authProvider(UserDetailsService service,
                                                          PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(service);
        p.setPasswordEncoder(encoder);
        return p;
    }
}
