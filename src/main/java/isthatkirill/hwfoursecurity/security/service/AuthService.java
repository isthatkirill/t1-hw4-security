package isthatkirill.hwfoursecurity.security.service;

import isthatkirill.hwfoursecurity.security.dto.JwtRequest;
import isthatkirill.hwfoursecurity.security.dto.JwtResponse;

/**
 * @author Kirill Emelyanov
 */

public interface AuthService {

    JwtResponse login(JwtRequest jwtRequest);

    JwtResponse refresh(String refreshToken);


}
