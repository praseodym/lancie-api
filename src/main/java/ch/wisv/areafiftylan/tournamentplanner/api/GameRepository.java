package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.tournamentplanner.games.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {}
