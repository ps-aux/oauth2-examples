package ps.aux.springsecoauth.oauth2setup.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import static java.util.Collections.singletonList;
import static ps.aux.springsecoauth.oauth2setup.authserver.OauthAuthenticationManager.createAuthenticationManager;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth.access-token-validity:8}")
    private int accessTokenValidity;

    @Value("${security.oauth.refresh-token-validity:720}")
    private int refreshTokenValidity;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static int HOURS = 60 * 60;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer
                .inMemory()
                .withClient("foo-client").secret("foo-secret")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(accessTokenValidity * HOURS)
                .refreshTokenValiditySeconds(refreshTokenValidity * HOURS)
                .scopes("read", "write")
                .resourceIds("foo")
                .and()
                .withClient("bar-client").secret("bar-secret")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(accessTokenValidity * HOURS)
                .refreshTokenValiditySeconds(refreshTokenValidity * HOURS)
                .scopes("read", "write")
                .resourceIds("bar");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(singletonList(accessTokenConverter));

        UserDetailsService userDetailsService = userDetailsService();
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(enhancerChain)
                .authenticationManager(createAuthenticationManager(userDetailsService, passwordEncoder));
    }

    @Bean
    public ProviderOAuthUserDetailsService userDetailsService() {
        return new ProviderOAuthUserDetailsService();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


}