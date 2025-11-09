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
    private  static final int POSITION_DECREMENT = 1;

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

    // CREATE playlist
    public Playlist create(String name, boolean isPublic, String imageUrl) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setPublic(isPublic);
        playlist.setImageUrl(imageUrl);
        playlist.setCreatedAt(Instant.now());
        return playlists.save(playlist);
    }

    // GET playlist by id
    public Optional<Playlist> get(Long id) {
        return playlists.findById(id);
    }

    // LIST playlists (paginated)
    public Page<Playlist> list(Pageable pageable) {
        return playlists.findAll(pageable);
    }

    // ADD track to playlist
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

    // LIST tracks in laylist as DTOs
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

    // DELETE playlist
    public boolean deletePlaylist(Long playlistId) {
        if (playlists.existsById(playlistId)) {
            playlists.deleteById(playlistId);

            return true;
        }
        return false;
    }

    // DELETE track from playlist
    @Transactional
    public boolean deleteTrack(Long playlistId, Long playlistTrackId) {
        Optional<PlaylistTrack> optional = playlistTracks.findById(playlistTrackId);

        if (!optional.isPresent()) {
            return false;
        }

        PlaylistTrack playlistTrack = optional.get();

        // Make sure the playlist we are deleting from is correct
        if (!playlistTrack.getPlaylist().getPlaylistId().equals(playlistId)) {
            return false;
        }

        int removedPosition = playlistTrack.getPosition();
        playlistTracks.delete(playlistTrack);

        // Reorder the remaining tracks
        List<PlaylistTrack> remainingTracks = playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId);

        for (PlaylistTrack track : remainingTracks) {
            if (track.getPosition() > removedPosition) {
                track.setPosition(track.getPosition() - POSITION_DECREMENT);
                playlistTracks.save(track);
            }
        }

        return true;
    }

    // COPY playlist
    public Playlist copyPlaylist(Long sourcePlaylistId) {
        Optional<Playlist> optional = playlists.findById(sourcePlaylistId);

        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Source playlist " + sourcePlaylistId + " not found.");
        }

        Playlist sourcePlaylist = optional.get();

        if (!sourcePlaylist.isPublic()) {
            throw new IllegalStateException("The selected playlist is private and cannot be saved");
        }

        // CREATE a new playlist
        Playlist copiedPlaylist = new Playlist();

        copiedPlaylist.setName(sourcePlaylist.getName());
        copiedPlaylist.setPublic(false);
        copiedPlaylist.setImageUrl(sourcePlaylist.getImageUrl());
        copiedPlaylist.setCreatedAt(Instant.now());

        playlists.save(copiedPlaylist);

        // Copy all tracks from the source playlist
        List<PlaylistTrack> sourceTracks = playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(sourcePlaylist.getPlaylistId());

        for (PlaylistTrack sourceTrack : sourceTracks) {
            PlaylistTrack newPlaylistTrack = new PlaylistTrack();

            newPlaylistTrack.setPlaylist(copiedPlaylist);
            newPlaylistTrack.setTrack(sourceTrack.getTrack());
            newPlaylistTrack.setPosition(sourceTrack.getPosition());
            newPlaylistTrack.setStartMs(sourceTrack.getStartMs());
            newPlaylistTrack.setEndMs(sourceTrack.getEndMs());

            playlistTracks.save(newPlaylistTrack);
        }

        return copiedPlaylist;
    }
}
