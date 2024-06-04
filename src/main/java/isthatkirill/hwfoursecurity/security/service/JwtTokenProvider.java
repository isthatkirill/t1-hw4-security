package isthatkirill.hwfoursecurity.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import isthatkirill.hwfoursecurity.error.exception.AccessDeniedException;
import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import isthatkirill.hwfoursecurity.security.service.props.JwtProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private SecretKey key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims().subject(username)
                .add("id", userId)
                .add("roles", buildStringList(roles))
                .build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder().claims(claims).issuedAt(now).expiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().subject(username)
                .add("id", userId).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder().claims(claims).issuedAt(now).expiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!isValidToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = getId(refreshToken);
        User user = checkIfUserExistsAndGet(userId);
        return JwtResponse.builder()
                .id(userId)
                .username(user.getUsername())
                .accessToken(createAccessToken(userId, user.getUsername(), user.getRoles()))
                .refreshToken(createRefreshToken(userId, user.getUsername()))
                .build();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean isValidToken(String token) {
        return !claims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    private String getUsername(String token) {
        return claims(token)
                .getPayload()
                .getSubject();
    }

    private Long getId(String token) {
        return claims(token)
                .getPayload()
                .get("id", Long.class);
    }

    private Jws<Claims> claims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    private List<String> buildStringList(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

}
