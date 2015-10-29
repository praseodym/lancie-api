package ch.wisv.areafiftylan.tournamentplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TournamentRegistrationClosedException extends RuntimeException {

    public TournamentRegistrationClosedException(Long tournamentId) {
        super("Registration for tournament " + tournamentId + " has been closed!");
    }
}