package isthatkirill.hwfoursecurity.web.controller;

import isthatkirill.hwfoursecurity.service.UserService;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import isthatkirill.hwfoursecurity.web.markers.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                          @PathVariable Long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping
    @PreAuthorize("@userSecurityExpression.canAccessAdminEndpoints()")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

}
