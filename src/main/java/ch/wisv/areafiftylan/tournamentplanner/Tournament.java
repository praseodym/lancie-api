package ch.wisv.areafiftylan.tournamentplanner;

import ch.wisv.areafiftylan.model.Team;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.tournamentplanner.games.Game;
import ch.wisv.areafiftylan.tournamentplanner.stage.Stage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(targetEntity = Team.class)
    Collection<Team> participants;

    @ManyToOne
    Game game;

    @Enumerated
    TournamentType tournamentType;

    LocalDateTime startingDateTime;

    @ManyToOne
    User crewMember;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Stage.class)
    Map<Integer, Stage> stages;

}
