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

    public Game(String title, String description, List<String> scoreAttributes, GameFormat format) {
        this.title = title;
        this.description = description;
        this.scoreAttributes = scoreAttributes;
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getScoreAttributes() {
        return scoreAttributes;
    }

    public void setScoreAttributes(List<String> scoreAttributes) {
        this.scoreAttributes = scoreAttributes;
    }

    public GameFormat getFormat() {
        return format;
    }

    public void setFormat(GameFormat format) {
        this.format = format;
    }
}
