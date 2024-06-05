package isthatkirill.hwfoursecurity.security.service;

import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void loginTest() {
        String password = "password";
        String username = "username";
        String accessToken = "access";
        String refreshToken = "refresh";
        Long id = 1L;

        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .roles(Collections.emptySet())
                .build();

        JwtRequest jwtRequest = JwtRequest.builder()
                .password(password)
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createAccessToken(user.getId(), username, user.getRoles())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(user.getId(), username)).thenReturn(refreshToken);

        JwtResponse response = authService.login(jwtRequest);

        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("accessToken", accessToken)
                .hasFieldOrPropertyWithValue("refreshToken", refreshToken)
                .hasFieldOrPropertyWithValue("username", username)
                .hasFieldOrPropertyWithValue("id", id);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );
    }

    @Test
    void loginByNonExistentUserTest() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .password("password")
                .username("username")
                .build();

        assertThrows(EntityNotFoundException.class, () -> authService.login(jwtRequest));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );

        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void refreshTest() {
        String oldRefreshToken = "oldRefresh";
        String newAccessToken = "newAccess";
        String newRefreshToken = "newRefresh";
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        when(jwtTokenProvider.refreshUserTokens(oldRefreshToken))
                .thenReturn(jwtResponse);

        JwtResponse testResponse = authService.refresh(oldRefreshToken);

        verify(jwtTokenProvider).refreshUserTokens(oldRefreshToken);
        assertThat(testResponse).isNotNull().isEqualTo(jwtResponse);
    }
}