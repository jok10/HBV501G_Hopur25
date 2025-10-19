package is.hi.hbv501g.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.TrackRepository;


@Service
public class SearchService {
    private final TrackRepository tracks;

    public SearchService(TrackRepository tracks) { this.tracks = tracks; }

    public Page<Track> search(String s, Pageable pageable) {
        if (s == null || s.isBlank()) {
            return Page.empty(pageable);
        }

        return tracks.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCase(s, s, s, pageable);
    }
}
