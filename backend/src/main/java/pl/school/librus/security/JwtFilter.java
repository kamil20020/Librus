package pl.school.librus.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.school.librus.exception.InvalidBearerTokenException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationContent = request.getHeader("Authorization");

        if(!hasToken(authorizationContent)){
            return;
        }

        String token = authorizationContent.substring(7);

        if(token == null || token.isBlank()){

            throw new InvalidBearerTokenException("Bearer token was not given");
        }

        Authentication authentication = getAuth();

        filterChain.doFilter(request, response);
    }

    private boolean hasToken(String authorizationContent){

        return authorizationContent != null &&
            !authorizationContent.isBlank() &&
            authorizationContent.startsWith("Bearer");
    }

    private Authentication getAuth() throws AuthenticationException {

        return null;
    }

}
