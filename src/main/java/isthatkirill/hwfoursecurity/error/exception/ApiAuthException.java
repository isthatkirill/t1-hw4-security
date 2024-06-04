package isthatkirill.hwfoursecurity.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Kirill Emelyanov
 */

@Getter
public class ApiAuthException extends AuthenticationException {

    private final HttpStatus httpStatus;

    public ApiAuthException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}