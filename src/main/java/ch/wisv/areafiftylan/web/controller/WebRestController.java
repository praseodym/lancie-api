package ch.wisv.areafiftylan.web.controller;

import ch.wisv.areafiftylan.web.model.CommitteeMember;
import ch.wisv.areafiftylan.web.model.Event;
import ch.wisv.areafiftylan.web.model.Sponsor;
import ch.wisv.areafiftylan.web.model.Tournament;
import ch.wisv.areafiftylan.web.service.EventServiceImpl;
import ch.wisv.areafiftylan.web.service.SponsorServiceImpl;
import ch.wisv.areafiftylan.web.service.TournamentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/web")
public class WebRestController {

    TournamentServiceImpl tournamentService;
    EventServiceImpl eventService;
    SponsorServiceImpl sponsorService;

    @Autowired
    public WebRestController(TournamentServiceImpl tournamentService, EventServiceImpl eventService,
                             SponsorServiceImpl sponsorService) {
        this.tournamentService = tournamentService;
        this.eventService = eventService;
        this.sponsorService = sponsorService;
    }

    @RequestMapping("/tournaments")
    public Collection<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @RequestMapping("/events")
    public Collection<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Returns all committee members. This is done in the Controller because it's simple and static nature
     *
     * @return A collection with all committee members.
     */
    @RequestMapping("/committee")
    public Collection<CommitteeMember> getCommittee(){
        Collection<CommitteeMember> committeeMembers = new ArrayList<>();
        committeeMembers.add(new CommitteeMember("Sille Kamoen", "Chairman", "people"));
        committeeMembers.add(new CommitteeMember("Rebecca Glans", "Secretary", "women"));
        committeeMembers.add(new CommitteeMember("Sven Popping", "Treasurer", "money"));

        return committeeMembers;
    }

    @RequestMapping("/sponsors")
    public Collection<Sponsor> getSponspors() {
        return sponsorService.getAllSponsors();
    }
}
