package ch.wisv.areafiftylan.tournamentplanner;

import ch.wisv.areafiftylan.model.Team;
import ch.wisv.areafiftylan.model.User;

public interface TournamentService {

    void registerForTournament(Tournament tournament, User user);

    void registerForTournament(Tournament tournament, Team team);

    void nextStage(Tournament tournament);

    void getNextMatch(Tournament tournament, Team team);
}
