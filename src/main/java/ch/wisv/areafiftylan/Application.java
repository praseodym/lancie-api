package ch.wisv.areafiftylan;


import ch.wisv.areafiftylan.model.User;
import ch.wisv.areafiftylan.model.util.Gender;
import ch.wisv.areafiftylan.service.repository.SeatRepository;
import ch.wisv.areafiftylan.service.repository.TeamRepository;
import ch.wisv.areafiftylan.service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    @Bean
    CommandLineRunner init(UserRepository accountRepository, TeamRepository teamRepository,
                           SeatRepository seatRepository) {
        //        User testUser = new User("testUser", "lolhash", "test@a5l.com");
        //        testUser = accountRepository.save(testUser);
        //
        //        for (int i = 1; i <= 3; i++) {
        //            for (int j = 1; j <= 5; j++) {
        //                Seat seat = new Seat(new Coordinate(i, j));
        //                if(seat.getCoordinate().equals(new Coordinate(1,1)))
        //                    seat.setUser(testUser);
        //                seatRepository.save(seat);
        //            }
        //
        //        }

        return (evt) -> {
            User testUser1 = new User("user", "passwordHash", "user@mail.com");
            testUser1.getProfile()
                    .setAllFields("Jan", "de Groot", "MonsterKiller9001", Gender.MALE, "Mekelweg 4", "2826CD", "Delft",
                            "0906-0666", null);
            User testUser2 = new User("user2", "passwordHash", "bert@mail.com");
            testUser2.getProfile()
                    .setAllFields("Bert", "Kleijn", "ILoveZombies", Gender.OTHER, "Mekelweg 20", "2826CD", "Amsterdam",
                            "0611", null);
            User testUser3 = new User("user3", "passwordHash", "katrien@ms.com");
            testUser3.getProfile()
                    .setAllFields("Katrien", "Zwanenburg", "Admiral Cheesecake", Gender.FEMALE, "Ganzenlaan 5",
                            "2826CD", "Duckstad", "0906-0666", null);
            User testUser4 = new User("user4", "passwordHash", "user@yahoo.com");
            testUser4.getProfile()
                    .setAllFields("Kees", "Jager", "l33tz0r", Gender.MALE, "Herenweg 2", "2826CD", "Delft",
                            "0902-30283", null);
            User testUser5 = new User("user5", "passwordHash", "custom@myself.com");
            testUser5.getProfile()
                    .setAllFields("Gert", "Gertson", "Whosyourdaddy", Gender.MALE, "Jansstraat", "8826CD", "Delft",
                            "0238-2309736", null);

            accountRepository.saveAndFlush(testUser1);
            accountRepository.saveAndFlush(testUser2);
            accountRepository.saveAndFlush(testUser3);
            accountRepository.saveAndFlush(testUser4);
            accountRepository.saveAndFlush(testUser5);

        };

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}