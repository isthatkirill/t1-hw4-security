package isthatkirill.hwfoursecurity.security.filter;

import isthatkirill.hwfoursecurity.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @MockBean
    private FilterChain filterChain;

    @MockBean
    private JwtTokenProvider provider;

    @Autowired
    private JwtTokenFilter filter;

    @Test
    void doFilterWithCorrectTokenTest() {
        String correctToken = "Bearer [correct]";
        String headerName = "Authorization";
        Authentication authentication = mock(Authentication.class);

        when(request.getHeader(headerName)).thenReturn(correctToken);
        when(provider.isValidToken(any())).thenReturn(true);
        when(provider.getAuthentication(any())).thenReturn(authentication);

        assertDoesNotThrow(() -> filter.doFilter(request, response, filterChain));

        verify(request, times(1)).getHeader(headerName);
        verify(provider, times(1)).isValidToken(correctToken.substring(7));
        verify(provider, times(1)).getAuthentication(correctToken.substring(7));
    }

    @Test
    void doFilterWithIncorrectTokenTest() {
        String incorrectToken = "[incorrect]";
        String headerName = "Authorization";
        Authentication authentication = mock(Authentication.class);

        when(request.getHeader(headerName)).thenReturn(incorrectToken);
        when(provider.isValidToken(any())).thenReturn(false);
        when(provider.getAuthentication(any())).thenReturn(authentication);

        assertDoesNotThrow(() -> filter.doFilter(request, response, filterChain));

        verify(request, times(1)).getHeader(headerName);
        verify(provider, times(1)).isValidToken(incorrectToken);
        verifyNoMoreInteractions(provider);
    }
}