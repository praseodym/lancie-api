package ch.wisv.areafiftylan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("could not find user '" + userId + "'.");
    }

    public UserNotFoundException(String email) {
        super("could not find user '" + email + "'.");
    }
}
