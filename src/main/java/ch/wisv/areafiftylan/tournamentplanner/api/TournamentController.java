package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.tournamentplanner.Tournament;
import ch.wisv.areafiftylan.tournamentplanner.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static ch.wisv.areafiftylan.util.ResponseEntityBuilder.createResponseEntity;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    TournamentService tournamentService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{tournamentId}/nextStage", method = RequestMethod.POST)
    public ResponseEntity<?> startNextStage(@PathVariable Long tournamentId) {
        Stage stage = tournamentService.startNextStage(tournamentId);
        return createResponseEntity(HttpStatus.OK, "Next stage started", stage);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/register/{tournamentId}", method = RequestMethod.POST)
    public ResponseEntity<?> registerForTournament(@PathVariable Long tournamentId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        tournamentService.registerForTournament(tournamentId, user);

        return createResponseEntity(HttpStatus.OK, "Succesfully registered for the tournament!");
    }
}
