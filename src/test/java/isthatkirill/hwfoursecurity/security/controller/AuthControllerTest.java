package isthatkirill.hwfoursecurity.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;
import isthatkirill.hwfoursecurity.security.dto.JwtTokenWrapper;
import isthatkirill.hwfoursecurity.security.service.AuthService;
import isthatkirill.hwfoursecurity.security.service.JwtTokenProvider;
import isthatkirill.hwfoursecurity.service.UserService;
import isthatkirill.hwfoursecurity.web.dto.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthenticationEntryPoint entryPoint;

    @MockBean
    private JwtTokenProvider provider;

    @Test
    @SneakyThrows
    void loginTest() {
        String username = "username";
        String password = "password";

        JwtRequest jwtRequest = JwtRequest.builder()
                .password(password)
                .username(username)
                .build();

        JwtResponse jwtResponse = JwtResponse.builder()
                .id(1L)
                .username(username)
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        when(authService.login(any())).thenReturn(jwtResponse);

        mvc.perform(post("/api/auth/login")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(jwtResponse.getId()))
                .andExpect(jsonPath("$.username").value(jwtResponse.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.getRefreshToken()));

        verify(authService, times(1)).login(jwtRequest);
    }

    @Test
    @SneakyThrows
    void loginWithNullPasswordTest() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .username("username")
                .build();

        mvc.perform(post("/api/auth/login")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(authService);
    }

    @Test
    @SneakyThrows
    void loginWithTooShortPasswordTest() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .username("username")
                .password("pa")
                .build();

        mvc.perform(post("/api/auth/login")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(authService);
    }

    @Test
    @SneakyThrows
    void registerTest() {
        UserDto userDto = UserDto.builder()
                .email("email@email.ru")
                .name("name")
                .username("username")
                .password("password")
                .build();

        when(userService.create(any())).thenReturn(userDto);

        mvc.perform(post("/api/auth/register")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.password").value(userDto.getPassword()));

        verify(userService).create(userDto);
    }

    @Test
    @SneakyThrows
    void registerWithTooShortPasswordTest() {
        UserDto userDto = UserDto.builder()
                .email("email@email.ru")
                .name("name")
                .username("username")
                .password("pa")
                .build();

        mvc.perform(post("/api/auth/register")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void registerWithInvalidEmailTest() {
        UserDto userDto = UserDto.builder()
                .email("ru.email.invalid")
                .name("name")
                .username("username")
                .password("password")
                .build();

        mvc.perform(post("/api/auth/register")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void refreshTest() {
        JwtTokenWrapper refresh = new JwtTokenWrapper("refresh");

        JwtResponse jwtResponse = JwtResponse.builder()
                .id(1L)
                .username("username")
                .accessToken("access")
                .refreshToken(refresh.getToken())
                .build();

        when(authService.refresh(anyString())).thenReturn(jwtResponse);

        mvc.perform(post("/api/auth/refresh")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(refresh))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(jwtResponse.getId()))
                .andExpect(jsonPath("$.username").value(jwtResponse.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.getRefreshToken()));

        verify(authService).refresh(refresh.getToken());
    }

    @Test
    @SneakyThrows
    void refreshWithNullTokenTest() {
        JwtTokenWrapper refresh = new JwtTokenWrapper("  ");

        mvc.perform(post("/api/auth/refresh")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(refresh))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(authService);
    }

}