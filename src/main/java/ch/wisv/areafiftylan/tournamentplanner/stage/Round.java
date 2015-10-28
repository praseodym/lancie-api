package ch.wisv.areafiftylan.tournamentplanner.stage;

import ch.wisv.areafiftylan.tournamentplanner.match.Match;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A round defines a number of matches with a given deadline, that should be all playable at once.
 */
public class Round {
    Collection<Match> matches;
    LocalDateTime deadline;
}
