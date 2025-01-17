package ch.wisv.areafiftylan.web.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Tournament extends Event {

    Sponsor sponsor;
    LinkedList<String> prizes;
    Format format;
    LocalDateTime startingDateTime;
    String rulePath;
    Collection<Platform> platform;

    public Tournament(String title, String subtitle, String headerTitle, String description, String backgroundImagePath,
                      Sponsor sponsor, LinkedList<String> prizes, Format format, LocalDateTime startingDateTime,
                      String rulePath, Platform... platforms) {
        super(title, subtitle, headerTitle, description, backgroundImagePath);
        this.sponsor = sponsor;
        this.prizes = prizes;
        this.format = format;
        this.startingDateTime = startingDateTime;
        this.rulePath = rulePath;
        this.platform = Arrays.asList(platforms);
    }

    public Collection<Platform> getPlatform() {
        return platform;
    }

    public void setPlatform(Collection<Platform> platform) {
        this.platform = platform;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public LinkedList<String> getPrizes() {
        return prizes;
    }

    public void setPrizes(LinkedList<String> prizes) {
        this.prizes = prizes;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public LocalDateTime getStartingDateTime() {
        return startingDateTime;
    }

    public void setStartingDateTime(LocalDateTime startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public String getRulePath() {
        return rulePath;
    }

    public void setRulePath(String rulePath) {
        this.rulePath = rulePath;
    }
}
