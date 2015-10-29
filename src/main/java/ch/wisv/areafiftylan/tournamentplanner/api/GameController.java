package ch.wisv.areafiftylan.tournamentplanner.api;

import ch.wisv.areafiftylan.tournamentplanner.games.Game;
import ch.wisv.areafiftylan.tournamentplanner.games.GameFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

import static ch.wisv.areafiftylan.util.ResponseEntityBuilder.createResponseEntity;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addNewGame(@RequestBody GameDTO gameDTO) {

        Game game = new Game(gameDTO.getTitle(), gameDTO.getDescription(), null, gameDTO.getFormat());

        game = gameRepository.saveAndFlush(game);

        // Create headers to set the location of the created User object.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(game.getId()).toUri());

        return createResponseEntity(HttpStatus.OK, httpHeaders,
                "Game successfully created at " + httpHeaders.getLocation(), game);
    }

    private class GameDTO {
        private String title;
        private String description;
        private GameFormat format;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public GameFormat getFormat() {
            return format;
        }
    }
}
