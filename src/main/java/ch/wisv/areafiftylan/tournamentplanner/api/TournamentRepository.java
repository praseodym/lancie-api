package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.tournamentplanner.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {


}
