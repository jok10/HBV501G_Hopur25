package is.hi.hbv501g.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.TrackRepository;


@Service
public class TrackService {
    private final TrackRepository tracks;

    public TrackService(TrackRepository tracks) { this.tracks = tracks; }

    public Page<Track> list(Pageable pageable) {
        return tracks.findAll(pageable);
    }
}
