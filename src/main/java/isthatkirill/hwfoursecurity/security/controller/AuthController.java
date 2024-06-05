package isthatkirill.hwfoursecurity.security.controller;

import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import isthatkirill.hwfoursecurity.security.service.AuthService;
import isthatkirill.hwfoursecurity.service.UserService;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import isthatkirill.hwfoursecurity.web.markers.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody @NotBlank String refreshToken) {
        return authService.refresh(refreshToken.trim());
    }

}
