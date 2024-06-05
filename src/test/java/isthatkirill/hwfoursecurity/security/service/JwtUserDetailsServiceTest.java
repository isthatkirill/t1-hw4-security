package isthatkirill.hwfoursecurity.security.service;

import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import isthatkirill.hwfoursecurity.security.model.JwtUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @Autowired
    private JwtUserDetailsService service;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameTest() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .name("name")
                .password("password")
                .email("email@email.ru")
                .roles(Set.of(Role.ROLE_USER))
                .build();

        Optional<User> optionalUser = Optional.of(user);
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();

        when(userRepository.findByUsername(any())).thenReturn(optionalUser);

        JwtUser jwtUser = (JwtUser) service.loadUserByUsername(user.getUsername());

        assertThat(jwtUser).isNotNull()
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("authorities", authorities);

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void loadUserByNonExistentUsernameTest() {
        String username = "username";
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

}