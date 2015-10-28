package ch.wisv.areafiftylan.tournamentplanner.stage;

import ch.wisv.areafiftylan.model.Team;

import java.util.Collection;
import java.util.List;

/**
 * Teams are divided as evenly as possible into a number of groups. Within each group, a miniature round robin is
 * played. This means that each Team plays a match against each other team.
 */
public class GroupStage extends Stage {

    Collection<Team> participants;

    List<Round> rounds;

    int currentRound;

    int maxTeamsPerGroup;
}
