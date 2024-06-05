package isthatkirill.hwfoursecurity.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Token wrapper")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenWrapper {

    @Schema(description = "Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2YWRpbV9xcSIsImlkIjozLCJpYXQi0fefqef...")
    @NotBlank(message = "Token cannot be blank")
    String token;

}
