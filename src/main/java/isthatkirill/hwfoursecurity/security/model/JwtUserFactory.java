package isthatkirill.hwfoursecurity.security.model;

import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

public class JwtUserFactory {

    public static JwtUser create(User user) {
        return JwtUser.builder()
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(buildAuthorities(user.getRoles()))
                .build();
    }

    private static List<GrantedAuthority> buildAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
