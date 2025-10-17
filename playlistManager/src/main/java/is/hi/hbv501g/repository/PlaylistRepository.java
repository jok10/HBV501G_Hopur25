package is.hi.hbv501g.repository;

import is.hi.hbv501g.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}