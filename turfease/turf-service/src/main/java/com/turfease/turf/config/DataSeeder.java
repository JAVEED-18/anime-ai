package com.turfease.turf.config;

import com.turfease.turf.model.SportType;
import com.turfease.turf.model.Turf;
import com.turfease.turf.repository.TurfRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedTurfs(TurfRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Turf t1 = new Turf();
                t1.setName("City Football Arena");
                t1.setLocation("Downtown");
                t1.setPricePerHour(50.0);
                t1.setSportType(SportType.FOOTBALL);
                t1.setImages("https://picsum.photos/seed/football/800/400");
                t1.setDescription("Premium 7-a-side football turf");

                Turf t2 = new Turf();
                t2.setName("Ace Badminton Court");
                t2.setLocation("North Block");
                t2.setPricePerHour(20.0);
                t2.setSportType(SportType.BADMINTON);
                t2.setImages("https://picsum.photos/seed/badminton/800/400");
                t2.setDescription("Indoor wooden court");

                repo.save(t1);
                repo.save(t2);
            }
        };
    }
}