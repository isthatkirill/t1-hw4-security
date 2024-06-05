package isthatkirill.hwfoursecurity.security.expression;

import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.security.model.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Service
public class UserSecurityExpression {

    public boolean canAccessUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isCorrectUserId(userId) || isAdmin(authentication);
    }

    public boolean canAccessAdminEndpoints() {
        return isAdmin(SecurityContextHolder.getContext().getAuthentication());
    }

    private boolean isCorrectUserId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser user = (JwtUser) authentication.getPrincipal();
        Long id = user.getId();
        return userId.equals(id);
    }

    private boolean isAdmin(Authentication authentication) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        return authentication.getAuthorities().contains(authority);
    }

}
