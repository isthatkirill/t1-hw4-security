package isthatkirill.hwfoursecurity.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.security.model.JwtUser;
import isthatkirill.hwfoursecurity.security.service.props.JwtProperties;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private JwtProperties jwtProperties;

    @MockBean
    private UserDetailsService userDetailsService;

    private SecretKey key;

    @PostConstruct
    void initSecretKeyAfterJwtPropertiesAutowiring() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    @Test
    void createAccessTokenTest() {
        Long userId = 1L;
        String username = "username";
        Set<Role> roles = Set.of(Role.ROLE_USER);
        List<String> rolesAsString = roles.stream()
                .map(Enum::name)
                .toList();

        String token = provider.createAccessToken(userId, username, roles);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        Claims payload = claims.getPayload();
        Long timeDiff = payload.getExpiration().getTime() - payload.getIssuedAt().getTime();

        assertThat(payload.getSubject()).isEqualTo(username);
        assertThat(payload.get("id", Long.class)).isEqualTo(userId);
        assertThat(payload.get("roles", List.class)).isEqualTo(rolesAsString);
        assertThat(timeDiff).isEqualTo(jwtProperties.getAccess());
    }

    @Test
    void createRefreshTokenTest() {
        Long userId = 1L;
        String username = "username";

        String token = provider.createRefreshToken(userId, username);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        Claims payload = claims.getPayload();
        Long timeDiff = payload.getExpiration().getTime() - payload.getIssuedAt().getTime();

        assertThat(payload.getSubject()).isEqualTo("username");
        assertThat(payload.get("id", Long.class)).isEqualTo(userId);
        assertThat(timeDiff).isEqualTo(jwtProperties.getRefresh());
    }

    @Test
    void refreshUserTokenWithExpiredTest() {
        Claims claims = Jwts.claims().subject("username").build();
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000);

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> provider.refreshUserTokens(token));
    }


    @Test
    void getAuthentication() {
        Claims claims = Jwts.claims().subject("username").build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000);
        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();

        JwtUser jwtUser = JwtUser.builder()
                .id(1L)
                .password("password")
                .name("name")
                .email("email")
                .username("username")
                .authorities(List.of(new SimpleGrantedAuthority("USER_ROLE"))).build();

        when(userDetailsService.loadUserByUsername(any())).thenReturn(jwtUser);

        Authentication authentication = provider.getAuthentication(token);
        JwtUser principal = (JwtUser) authentication.getPrincipal();

        assertThat(principal).isNotNull()
                .isEqualTo(jwtUser);
    }

    @Test
    void isValidTokenBadCaseTest() {
        Claims claims = Jwts.claims().subject("username").build();
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000);
        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> provider.isValidToken(token));
    }

    @Test
    void isValidTokenGoodCaseTest() {
        Claims claims = Jwts.claims().subject("username").build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000);
        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();

        boolean isValid = provider.isValidToken(token);
        assertThat(isValid).isTrue();
    }

}