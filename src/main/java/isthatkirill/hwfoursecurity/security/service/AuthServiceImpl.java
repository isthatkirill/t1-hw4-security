package isthatkirill.hwfoursecurity.security.service;

import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.getUsername(),
                jwtRequest.getPassword()
        ));
        User user = checkIfUserExistsAndGet(jwtRequest.getUsername());
        log.info("User with username={} logged in", user.getUsername());
        return JwtResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .build();
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    private User checkIfUserExistsAndGet(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, username));
    }

}
