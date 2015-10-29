package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.model.Team;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.tournamentplanner.Tournament;
import ch.wisv.areafiftylan.tournamentplanner.stage.Stage;

import java.util.Collection;

public interface TournamentService {

    void registerForTournament(Long tournamentId, User user);

    void registerForTournament(Long tournamentId, Team team);

    void nextStage(Tournament tournament);

    void getNextMatch(Tournament tournament, Team team);

    Collection<Tournament> getAllTournaments();

    Stage startNextStage(Long tournamentId);
}
