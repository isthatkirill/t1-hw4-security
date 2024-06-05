package isthatkirill.hwfoursecurity.service.impl;

import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public UserDto create(UserDto userDto) {
        checkForUnique(userDto);
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user = userRepository.save(user);
        log.info("New user added --> {}", userDto);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long userId) {
        log.info("Get user by id={}", userId);
        return userMapper.toDto(checkIfExistsAndGet(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long userId) {
        User user = checkIfExistsAndGet(userId);
        checkForUnique(userDto);
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        log.info("User with id={} updated --> {}", userId, userDto);
        return userMapper.toDto(user);
    }


    private void checkForUnique(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent() ||
                userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserNotUniqueException("Users with the same name or email already exist.");
        }
    }

    private User checkIfExistsAndGet(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userId));
    }

}
