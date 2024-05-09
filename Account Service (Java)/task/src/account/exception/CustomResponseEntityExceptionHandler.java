package account.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler({ UserExistsException.class, NewPasswordMustBeDifferentException.class,
            PasswordBreachedException.class, UserDoesNotExistsException.class, InvalidPeriodException.class})
    public ResponseEntity<RestError> handleUserExistException(BaseException ex, HttpServletRequest request) {
        ResponseStatus responseStatusAnnotation = ex.getClass().getAnnotation(ResponseStatus.class);
        RestError re = new RestError(ex.getTimestamp(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), responseStatusAnnotation.reason(), request.getServletPath());
        return new ResponseEntity<>(re, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<RestError> handleRuntimeException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String err = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList().get(0);
        RestError re = new RestError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), err, request.getServletPath());
        return new ResponseEntity<>(re, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JdbcSQLIntegrityConstraintViolationException.class})
    public ResponseEntity<RestError> handleJdbcSQLIntegrityConstraintViolationException(JdbcSQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
        RestError re = new RestError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(re, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<RestError> handleJdbcSQLIntegrityConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        RestError re = new RestError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getServletPath());
        return new ResponseEntity<>(re, HttpStatus.BAD_REQUEST);
    }
}