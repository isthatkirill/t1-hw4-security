package isthatkirill.hwfoursecurity.security.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    Long id;
    String username;
    String accessToken;
    String refreshToken;

}
