package isthatkirill.hwfoursecurity.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "UserController", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get information about yourself. For getting information about other users " +
            "ADMIN role is required. ")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Update user", description = "Update information about yourself. For updating information" +
            " about other users ADMIN role is required.")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                          @PathVariable Long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping
    @PreAuthorize("@userSecurityExpression.canAccessAdminEndpoints()")
    @Operation(summary = "Get all users", description = "ADMIN role is required. Returns a list of all users.")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

}
