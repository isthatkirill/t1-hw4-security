package isthatkirill.hwfoursecurity.security.dto;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {

    @NotBlank(message = "Username cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters")
    String username;

    @NotBlank(message = "Password cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters")
    String password;

}
