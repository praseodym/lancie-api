package ch.wisv.areafiftylan.tournamentplanner.games;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String title;

    String description;

    @ElementCollection
    List<String> scoreAttributes;

    @Enumerated
    GameFormat format;
}
