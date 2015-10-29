package ch.wisv.areafiftylan.tournamentplanner;

import ch.wisv.areafiftylan.model.Team;
import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.tournamentplanner.games.Game;
import ch.wisv.areafiftylan.tournamentplanner.stage.GroupStage;
import ch.wisv.areafiftylan.tournamentplanner.stage.SignupStage;
import ch.wisv.areafiftylan.tournamentplanner.stage.Stage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

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
    LinkedList<Stage> stages;

    int currentStage = 0;

    public void addStage(Stage stage) {
        this.stages.add(stage);
    }

    public Tournament(TournamentType tournamentType, LocalDateTime startingDateTime, Game game) {
        this.tournamentType = tournamentType;
        this.startingDateTime = startingDateTime;
        this.game = game;
    }

    public static Tournament initialize(TournamentType type, Game game) {
        Tournament tournament = new Tournament(type, LocalDateTime.now(), game);
        switch (type) {
            case ROUND_ROBIN:
                return makeRoundRobin(tournament);
        }
        return null;
    }

    public Collection<Team> getParticipants() {
        return participants;
    }

    public void addParticipant(Team team) {
        this.participants.add(team);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
    }

    public LocalDateTime getStartingDateTime() {
        return startingDateTime;
    }

    public void setStartingDateTime(LocalDateTime startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public User getCrewMember() {
        return crewMember;
    }

    public void setCrewMember(User crewMember) {
        this.crewMember = crewMember;
    }

    public LinkedList<Stage> getStages() {
        return stages;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    private static Tournament makeRoundRobin(Tournament tournament) {
        Stage signupStage = new SignupStage();
        tournament.addStage(signupStage);
        Stage groupStage = new GroupStage();
        tournament.addStage(groupStage);
        return tournament;
    }

}
