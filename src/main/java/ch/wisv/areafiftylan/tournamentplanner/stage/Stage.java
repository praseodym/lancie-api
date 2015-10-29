package ch.wisv.areafiftylan.tournamentplanner.stage;

import javax.persistence.*;

/**
 * The Stage class is the base class for all stages of a tournament. Examples of stages are Group Stage, Single
 * Elimination, Double Elimination and other custom ones. These can all be added to a Tournament.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public abstract void start();
}
