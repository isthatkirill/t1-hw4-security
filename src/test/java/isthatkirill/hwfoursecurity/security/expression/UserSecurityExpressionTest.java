package isthatkirill.hwfoursecurity.security.expression;

import isthatkirill.hwfoursecurity.security.model.JwtUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserSecurityExpressionTest {

    @Autowired
    private UserSecurityExpression userSecurityExpression;

    @Test
    void canAccessUserWithCorrectIdTest() {
        Long userId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = userSecurityExpression.canAccessUser(userId);

        assertThat(result).isTrue();
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    void canAccessUserWithIncorrectIdTest() {
        Long userId = 1L;
        Long authUserId = 2L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = userSecurityExpression.canAccessUser(authUserId);

        assertThat(result).isFalse();
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void canAccessAdminEndpointsByAdminTest() {
        boolean result = userSecurityExpression.canAccessAdminEndpoints();
        assertThat(result).isTrue();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void canAccessAdminEndpointsByUserTest() {
        boolean result = userSecurityExpression.canAccessAdminEndpoints();
        assertThat(result).isFalse();
    }

}