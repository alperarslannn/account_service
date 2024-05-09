package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found!")
public class RoleNotFoundException extends BaseException {
    private final LocalDateTime timestamp;

    public RoleNotFoundException() {
        super();
        timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
