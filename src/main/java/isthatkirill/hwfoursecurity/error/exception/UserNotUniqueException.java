package isthatkirill.hwfoursecurity.error.exception;

/**
 * @author Kirill Emelyanov
 */

public class UserNotUniqueException extends RuntimeException {

    public UserNotUniqueException(String message) {
        super(message);
    }

}
