package isthatkirill.hwfoursecurity.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import isthatkirill.hwfoursecurity.security.dto.JwtTokenWrapper;
import isthatkirill.hwfoursecurity.security.service.AuthService;
import isthatkirill.hwfoursecurity.service.UserService;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import isthatkirill.hwfoursecurity.web.markers.OnCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "Endpoints for login and registration")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Valid @RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh")
    public JwtResponse refresh(@RequestBody @Valid JwtTokenWrapper refreshToken) {
        return authService.refresh(refreshToken.getToken());
    }

}
