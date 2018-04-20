package ps.aux.springsecoauth.oauth2setup.resourceserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * NOTE: Do not use @EnableResourceServer
 * <p>
 * Source:
 * https://github.com/spring-projects/spring-security-oauth/blob/master/tests/annotation/multi/src/main/java/demo/Application.java
 * https://stackoverflow.com/questions/28861189/oauth2-sso-for-multiple-resource-servers-with-spring-boot-and-jhipster
 */
@Configuration
public class ResourceServerConfig {

    private ResourceServerConfiguration resource() {
        return new ResourceServerConfiguration() {
            // Switch off the Spring Boot @Autowired configurers
            public void setConfigurers(List<ResourceServerConfigurer> configurers) {
                super.setConfigurers(configurers);
            }
        };
    }

    @Bean
    protected ResourceServerConfiguration fooResources() {
        ResourceServerConfiguration resource = resource();
        resource.setConfigurers(singletonList(new ResourceServerConfigurerAdapter() {

            @Override
            public void configure(ResourceServerSecurityConfigurer resources) {
                resources.resourceId("foo");
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.antMatcher("/foo/**").authorizeRequests().anyRequest()
                        .hasAuthority("FOO");
            }

        }));
        resource.setOrder(3);

        return resource;

    }

    @Bean
    protected ResourceServerConfiguration barResources() {

        ResourceServerConfiguration resource = new ResourceServerConfiguration() {
            // Switch off the Spring Boot @Autowired configurers
            public void setConfigurers(List<ResourceServerConfigurer> configurers) {
                super.setConfigurers(configurers);
            }
        };

        resource.setConfigurers(singletonList(new ResourceServerConfigurerAdapter() {

            @Override
            public void configure(ResourceServerSecurityConfigurer resources) {
                resources.resourceId("bar");
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest()
                        .hasAuthority("BAR");
            }
        }));

        resource.setOrder(4);

        return resource;

    }
}
