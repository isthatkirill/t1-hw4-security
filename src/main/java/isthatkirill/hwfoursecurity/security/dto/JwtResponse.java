package isthatkirill.hwfoursecurity.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT response")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    @Schema(description = "Id", example = "1")
    Long id;

    @Schema(description = "Username", example = "isthatkirill")
    String username;

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpc3RoYXRraXJpbGwiLCJpZCI6MSwicm...")
    String accessToken;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpc3RoYXRraXJpbGwiLCJpZCI6MSwia...")
    String refreshToken;

}
