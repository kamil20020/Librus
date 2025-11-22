package pl.school.librus.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.school.librus.user.UserService;

@Configuration
@RequiredArgsConstructor
public class AuthProviderConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Bean
    public AuthenticationProvider daoAuthProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);

        return provider;
    }
}
