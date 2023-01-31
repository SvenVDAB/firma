package be.vdab.firma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    private static final String GEBRUIKER = "gebruiker";
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcUserDetailsManager maakPrinciples() {

        var manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("""
                select emailAdres as username, paswoord as password, true as enabled
                from werknemers where emailadres = ?                          
                """
        );
        manager.setAuthoritiesByUsernameQuery("""
                select emailadres as username, 'gebruiker' as authorities 
                from werknemers where emailadres = ?              
                """);
        return manager;
    }

    @Bean
    public WebSecurityCustomizer configureerWeb() {
        return (web) -> web.ignoring().mvcMatchers("/images/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain geefRechten(HttpSecurity http) throws Exception {
        http.formLogin(login -> login.loginPage("/login"));
        http.authorizeRequests(requests -> requests
                .mvcMatchers("/seven")
                .hasAuthority(GEBRUIKER)
                .mvcMatchers("/", "/login").permitAll()
                .mvcMatchers("/**").authenticated()
        );
        return http.build();
    }
}
