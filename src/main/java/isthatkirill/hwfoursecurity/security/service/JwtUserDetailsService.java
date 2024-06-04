package isthatkirill.hwfoursecurity.security.service;

import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private User checkIfUserExistsAndGet(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, username));
    }
}
