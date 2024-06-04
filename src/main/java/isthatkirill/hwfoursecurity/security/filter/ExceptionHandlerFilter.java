package isthatkirill.hwfoursecurity.security.filter;

import io.jsonwebtoken.JwtException;
import isthatkirill.hwfoursecurity.error.exception.ApiAuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Kirill Emelyanov
 */

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        AuthenticationException authE;

        try {
            filterChain.doFilter(request, response);
            return;
        } catch (JwtException exception) {
            authE = new ApiAuthException(
                    exception.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (AuthenticationException e) {
            authE = e;
        }

        authenticationEntryPoint.commence(
                request,
                response,
                authE
        );
    }

}