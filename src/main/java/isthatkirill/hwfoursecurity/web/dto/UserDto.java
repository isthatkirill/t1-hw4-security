package isthatkirill.hwfoursecurity.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import isthatkirill.hwfoursecurity.web.markers.OnCreate;
import isthatkirill.hwfoursecurity.web.markers.OnUpdate;
import jakarta.validation.constraints.Email;
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
@Schema(description = "User DTO")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @Schema(description = "Name", example = "Kirill", minLength = 4, maxLength = 255)
    @NotBlank(message = "Name cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the name must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "Username", example = "isthatkirill", minLength = 4, maxLength = 255)
    @NotBlank(message = "Username cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Schema(description = "User email", example = "isthatkirill2025@yandex.ru", pattern = "Email pattern")
    @NotBlank(message = "Email cannot be blank or null", groups = {OnCreate.class})
    @Email(message = "Email must satisfy pattern", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Schema(description = "User password", example = "$2a$10$XuXAvrwHuN1zGvG/cuUZI.0W60KuJgFJ9JpWqyJ2tFCd/MEb5QtT6",
            minLength = 4, maxLength = 255)
    @NotBlank(message = "Password cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the password must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

}
