package is.hi.hbv501g;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ApplicationRunner;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.TrackRepository;


@Configuration
public class DevData {
    @Bean
    ApplicationRunner addSampleTracks(TrackRepository tracks) {
        return new ApplicationRunner() {
            @Override
            public void run(org.springframework.boot.ApplicationArguments args) {
                if (tracks.count() == 0) {
                    String[][] sample_tracks = {
                            {"Darkspace", "Dark 3.12", "Dark Space III", "640000"},
                            {"Satyricon", "Mother North", "Nemesis Divina", "382000"},
                            {"Unleashed", "Demoneater", "Hell's Unleashed", "233000"},
                            {"Whoredom Rife", "Beyond The Skies Of God", "Dommedagskvad", "357000"}
                    };

                    for (String[] sample_track : sample_tracks) {
                        Track track = new Track();

                        track.setArtist(sample_track[0]);
                        track.setTitle(sample_track[1]);
                        track.setAlbum(sample_track[2]);
                        track.setDurationMs(Long.parseLong(sample_track[3]));

                        tracks.save(track);
                    }
                }
            }
        };
    }
}
