package is.hi.hbv501g.repository;

import is.hi.hbv501g.domain.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TrackRepository extends JpaRepository<Track, Long> {
    Page<Track> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String s1, String s2, Pageable pageable);
}
