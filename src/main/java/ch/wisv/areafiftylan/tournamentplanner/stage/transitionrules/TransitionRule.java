package ch.wisv.areafiftylan.tournamentplanner.stage.transitionrules;

import ch.wisv.areafiftylan.model.Team;

import java.util.Collection;

/**
 * Transition rules determine which groups move on to the next stage. A Transition Rule determines this from the raw
 * stage data. This way, there is no fixed method of promoting teams to the next Stage. For example, a team can progress
 * with the fastests time or highest score without having to change the GroupStage itself.
 */
public interface TransitionRule {

    Collection<Team> getBestX(int x);
}
