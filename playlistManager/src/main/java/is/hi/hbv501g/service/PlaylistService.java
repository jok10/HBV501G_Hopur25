package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.domain.PlaylistTrack;
import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.PlaylistRepository;
import is.hi.hbv501g.repository.PlaylistTrackRepository;
import is.hi.hbv501g.repository.TrackRepository;
import is.hi.hbv501g.web.dto.PlaylistTrackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaylistService {

    private final PlaylistRepository playlists;
    private final TrackRepository tracks;
    private final PlaylistTrackRepository playlistTracks;

    public PlaylistService(PlaylistRepository playlists,
                           TrackRepository tracks,
                           PlaylistTrackRepository playlistTracks) {
        this.playlists = playlists;
        this.tracks = tracks;
        this.playlistTracks = playlistTracks;
    }

    public Playlist create(String name, boolean isPublic, String imageUrl) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setPublic(isPublic);
        playlist.setImageUrl(imageUrl);
        playlist.setCreatedAt(Instant.now());
        return playlists.save(playlist);
    }

    public Optional<Playlist> get(Long id) {
        return playlists.findById(id);
    }

    public Page<Playlist> list(Pageable pageable) {
        return playlists.findAll(pageable);
    }

    public boolean delete(Long id) {
        if (playlists.existsById(id)) {
            playlists.deleteById(id);
            return true;
        }
        return false;
    }

    public PlaylistTrack addTrack(Long playlistId, Long trackId) {
        Playlist p = playlists.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + playlistId));
        Track t = tracks.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found: " + trackId));

        int nextPos = playlistTracks.countByPlaylist_PlaylistId(playlistId);

        PlaylistTrack pt = new PlaylistTrack();
        pt.setPlaylist(p);
        pt.setTrack(t);
        pt.setPosition(nextPos);
        pt.setStartMs(0L);
        pt.setEndMs(t.getDurationMs());
        return playlistTracks.save(pt);
    }

    @Transactional(readOnly = true)
    public List<PlaylistTrackResponse> listTracksDTO(Long playlistId) {
        return playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId)
                .stream()
                .map(pt -> {
                    PlaylistTrackResponse dto = new PlaylistTrackResponse();
                    dto.setPlaylistTrackId(pt.getPlaylistTrackId());
                    dto.setPlaylistId(pt.getPlaylist().getPlaylistId());
                    dto.setTrackId(pt.getTrack().getTrackId());
                    dto.setPosition(pt.getPosition());
                    dto.setStartMs(pt.getStartMs());
                    dto.setEndMs(pt.getEndMs());
                    return dto;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaylistTrack> listTracks(Long playlistId) {
        return playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId);
    }
}
