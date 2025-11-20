package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.TrackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackService {
    private final TrackRepository tracks;

    public TrackService(TrackRepository tracks) { this.tracks = tracks; }

    public Page<Track> list(Pageable pageable) { return tracks.findAll(pageable); }

    public Track create(String title, String artist, String album, Long durationMs) {
        Track t = new Track();
        t.setTitle(title);
        t.setArtist(artist);
        t.setAlbum(album);
        t.setDurationMs(durationMs != null ? durationMs : 0L);
        return tracks.save(t);
    }

    @Transactional
    public boolean delete(Long id) {
        if (tracks.existsById(id)) {
            tracks.deleteById(id);
            return true;
        }
        return false;
    }

}
