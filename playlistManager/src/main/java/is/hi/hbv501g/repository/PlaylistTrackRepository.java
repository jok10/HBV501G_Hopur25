package is.hi.hbv501g.repository;

import is.hi.hbv501g.domain.PlaylistTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, Long> {
    int countByPlaylistId(Long playlistId);
    List<PlaylistTrack> findByPlaylistIdOrderByIdAsc(Long playlistId);
}