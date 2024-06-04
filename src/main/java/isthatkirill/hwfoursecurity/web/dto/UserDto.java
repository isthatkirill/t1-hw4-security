package isthatkirill.hwfoursecurity.web.dto;

import isthatkirill.hwfoursecurity.web.markers.OnCreate;
import isthatkirill.hwfoursecurity.web.markers.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @NotBlank(message = "Name cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the name must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "Username cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @NotBlank(message = "Email cannot be blank or null", groups = {OnCreate.class})
    @Email(message = "Email must satisfy pattern", groups = {OnCreate.class, OnUpdate.class})
    private String email;


    @NotBlank(message = "Password cannot be blank or null", groups = {OnCreate.class})
    @Length(min = 4, max = 255, message = "Length of the password must be from 4 to 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

}
