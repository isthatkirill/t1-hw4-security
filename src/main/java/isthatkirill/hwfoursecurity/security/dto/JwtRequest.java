package isthatkirill.hwfoursecurity.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {

    @Schema(description = "Username", example = "isthatkirill", minLength = 4, maxLength = 255)
    @NotBlank(message = "Username cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters")
    String username;

    @Schema(description = "User password", example = "admin", minLength = 4, maxLength = 255)
    @NotBlank(message = "Password cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters")
    String password;

}
