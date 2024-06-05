package isthatkirill.hwfoursecurity.service;

import isthatkirill.hwfoursecurity.web.dto.UserDto;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto getById(Long userId);

    List<UserDto> getAll();

    UserDto update(UserDto userDto, Long userId);

}
