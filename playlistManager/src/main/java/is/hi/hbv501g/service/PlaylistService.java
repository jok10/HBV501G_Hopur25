package is.hi.hbv501g.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.repository.PlaylistRepository;


@Service
public class PlaylistService {
    private final PlaylistRepository playlists;

    public PlaylistService(PlaylistRepository playlists) { this.playlists = playlists; }

    public Playlist create(String name, boolean isPublic, String imageUrl) {
        Playlist playlist = new Playlist();

        playlist.setName(name);
        playlist.setPublic(isPublic);
        playlist.setImageUrl(imageUrl);
        playlist.setCreatedAt(Instant.now());

        return playlists.save(playlist);
    }

    public Optional<Playlist> get(Long id) {
        return playlists.findById(id);          // Search the DB for a playlist with id as the primary key
    }

    public Page<Playlist> list(Pageable pageable) {
        return playlists.findAll(pageable);
    }

    // Delete playlist
    public boolean delete(Long id) {
        if (playlists.existsById(id)) {
            playlists.deleteById(id);

            return true;
        }

        return false;
    }
}
