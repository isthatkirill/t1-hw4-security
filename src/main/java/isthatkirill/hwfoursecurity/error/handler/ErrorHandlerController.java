package isthatkirill.hwfoursecurity.error.handler;

import isthatkirill.hwfoursecurity.error.exception.AccessDeniedException;
import isthatkirill.hwfoursecurity.error.exception.EntityNotFoundException;
import isthatkirill.hwfoursecurity.error.exception.UserNotUniqueException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundHandle(final EntityNotFoundException e) {
        log.error("Error: {} Description: {}", HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userNotUniqueHandle(final UserNotUniqueException e) {
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notValidArgumentHandle(final MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String errorMessage = errors.stream()
                .map(error -> String.format("Field: [%s] error: [%s], value: [%s]",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.joining("\n"));
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationHandle(final ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> String.format("Field: [%s], error: [%s], value: [%s]",
                        violation.getPropertyPath().toString(), violation.getMessage(), violation.getInvalidValue()))
                .collect(Collectors.joining("\n"));
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDeniedHandle(final Exception e) {
        log.error("Error: {} Description: {}", HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse authErrorHandle(final AuthenticationException e) {
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unexpectedErrorHandle(final Exception e) {
        log.error("Error: {} Description: {}", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
    }



}
