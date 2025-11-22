package pl.school.librus.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.school.librus.exception.InvalidBearerTokenException;
import pl.school.librus.security.JwtService;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationContent = request.getHeader("Authorization");

        if(!hasToken(authorizationContent)){

            filterChain.doFilter(request, response);

            return;
        }

        String token = authorizationContent.substring(7);

        if(token == null || token.isBlank()){

            throw new InvalidBearerTokenException("Bearer token was not given");
        }

        Authentication authentication = getAuth(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean hasToken(String authorizationContent){

        return authorizationContent != null &&
            !authorizationContent.isBlank() &&
            authorizationContent.startsWith("Bearer");
    }

    private Authentication getAuth(String token) throws AuthenticationException {

        String username = jwtService.extractUsername(token, "token");

        UserDetails gotUser = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
            gotUser,
            null,
            gotUser.getAuthorities()
        );
    }
}
