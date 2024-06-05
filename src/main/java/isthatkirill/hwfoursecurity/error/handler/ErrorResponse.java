package isthatkirill.hwfoursecurity.error.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@Getter
@AllArgsConstructor
@Schema(description = "Error response")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    @Schema(description = "error", example = "Forbidden")
    String error;

    @Schema(description = "description", example = "Access denied")
    String description;

}