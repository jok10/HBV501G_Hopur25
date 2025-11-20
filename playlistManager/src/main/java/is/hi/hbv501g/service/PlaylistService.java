package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.domain.PlaylistTrack;
import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.domain.User;
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
    public Playlist create(String name, boolean isPublic, String imageUrl, User owner) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setPublic(isPublic);
        playlist.setImageUrl(imageUrl);
        playlist.setCreatedAt(Instant.now());
        playlist.setOwner(owner);
        return playlists.save(playlist);
    }


    // list all playlists for a given owner

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
                    dto.setTrack(pt.getTrack());
                    return dto;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaylistTrack> listTracks(Long playlistId) {
        return playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId);
    }
    @Transactional(readOnly = true)
    public List<Playlist> listByOwner(User owner) {
        return playlists.findByOwner(owner);
    }

    // DELETE playlist
    // DELETE playlist
    public boolean deletePlaylist(Long playlistId) {
        // Check if playlist exists
        Optional<Playlist> opt = playlists.findById(playlistId);
        if (!opt.isPresent()) {
            return false;
        }

        // 1) Delete all playlist tracks for this playlist
        List<PlaylistTrack> pts =
                playlistTracks.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId);
        if (!pts.isEmpty()) {
            playlistTracks.deleteAll(pts);
        }

        // 2) Delete the playlist itself
        playlists.delete(opt.get());

        return true;
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

    // UPDATE start/end markers for a track in a playlist
    @Transactional
    public PlaylistTrackResponse updateTrackMarkers(Long playlistId, Long playlistTrackId, Long startMs, Long endMs) {
        Optional<PlaylistTrack> optional = playlistTracks.findById(playlistTrackId);

        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Track " + playlistTrackId + " not found.");
        }

        PlaylistTrack playlistTrack = optional.get();

        if (!playlistTrack.getPlaylist().getPlaylistId().equals(playlistId)) {
            throw new IllegalArgumentException("Track " + playlistTrackId + " not in this playlist.");
        }

        long duration = playlistTrack.getTrack().getDurationMs();
        long defaultStart = (startMs != null) ? startMs : 0L;
        long defaultEnd = (endMs != null) ? endMs : duration;

        if (defaultStart < 0 || defaultEnd <= defaultStart || defaultEnd > duration) {
            throw new IllegalArgumentException("Invalid marker values.");
        }

        playlistTrack.setStartMs(defaultStart);
        playlistTrack.setEndMs(defaultEnd);
        playlistTracks.save(playlistTrack);

        // DTO to return
        PlaylistTrackResponse dto = new PlaylistTrackResponse();

        dto.setPlaylistTrackId(playlistTrack.getPlaylistTrackId());
        dto.setPlaylistId(playlistTrack.getPlaylist().getPlaylistId());
        dto.setTrackId(playlistTrack.getTrack().getTrackId());
        dto.setPosition(playlistTrack.getPosition());
        dto.setStartMs(playlistTrack.getStartMs());
        dto.setEndMs(playlistTrack.getEndMs());

        return dto;
    }
}
