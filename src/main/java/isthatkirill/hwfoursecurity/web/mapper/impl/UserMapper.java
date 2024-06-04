package isthatkirill.hwfoursecurity.web.mapper.impl;

import isthatkirill.hwfoursecurity.model.User;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import isthatkirill.hwfoursecurity.web.mapper.Mappable;
import org.mapstruct.Mapper;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

}
