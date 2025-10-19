package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


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
}
