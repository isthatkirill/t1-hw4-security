package isthatkirill.hwfoursecurity.service.impl;

import isthatkirill.hwfoursecurity.error.exception.UserNotUniqueException;
import isthatkirill.hwfoursecurity.model.Role;
import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.repository.UserRepository;
import isthatkirill.hwfoursecurity.service.UserService;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import isthatkirill.hwfoursecurity.web.mapper.impl.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(UserDto userDto) {
        checkForUnique(userDto);
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user = userRepository.save(user);
        log.info("New user added --> {}", userDto);
        return userMapper.toDto(user);
    }

    private void checkForUnique(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent() ||
                userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserNotUniqueException("Users with the same name or email already exist.");
        }
    }

}
