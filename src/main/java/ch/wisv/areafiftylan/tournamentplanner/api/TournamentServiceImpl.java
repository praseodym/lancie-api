package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.model.Team;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.service.TeamService;
import ch.wisv.areafiftylan.tournamentplanner.Tournament;
import ch.wisv.areafiftylan.tournamentplanner.exception.TournamentRegistrationClosedException;
import ch.wisv.areafiftylan.tournamentplanner.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    TeamService teamService;

    @Autowired
    TournamentRepository tournamentRepository;

    @Override
    public void registerForTournament(Long tournamentId, User user) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        if (tournament.getCurrentStage() != 0) {
            throw new TournamentRegistrationClosedException(tournamentId);
        } else {
            //FIXME: Streamline User to Team conversion;
            Team virtualTeam = new Team(user.getProfile().getDisplayName(), user);
            virtualTeam = teamService.save(virtualTeam);
            tournament.addParticipant(virtualTeam);
        }
    }

    @Override
    public void registerForTournament(Long tournamentId, Team team) {

    }

    @Override
    public void nextStage(Tournament tournament) {

    }

    @Override
    public void getNextMatch(Tournament tournament, Team team) {

    }

    @Override
    public Collection<Tournament> getAllTournaments() {
        return null;
    }

    @Override
    public Stage startNextStage(Long tournamentId) {
        return null;
    }
}
