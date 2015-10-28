package ch.wisv.areafiftylan.tournamentplanner.match;

import ch.wisv.areafiftylan.model.Team;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * A Match resembles a match between two teams. Each round consists of a number of concurrent matches. Matches have a
 * deadline before which matches have to be played. Results are also stored in the Match, in a Game specific manner.
 */
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Team.class)
    Team teamA;

    @ManyToOne(targetEntity = Team.class)
    Team teamB;

    //TODO: Make this dependent on the Game of the Tournament
    //MatchResult result;

    @Enumerated
    MatchStatus status;

    LocalDateTime deadline;

    int roundNumber;
}
