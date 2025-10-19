package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.PlaylistTrack;
import is.hi.hbv501g.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<PlaylistTrack> addTrack(@PathVariable Long playlistId,
                                                  @RequestParam Long trackId) {
        PlaylistTrack pt = playlistService.addTrack(playlistId, trackId);
        return ResponseEntity
                .created(URI.create("/api/playlists/" + playlistId + "/tracks/" + pt.getPlaylistTrackId()))
                .body(pt);
    }

    @GetMapping("/{playlistId}/tracks")
    public List<PlaylistTrack> listTracks(@PathVariable Long playlistId) {
        return playlistService.listTracks(playlistId);
    }
}
