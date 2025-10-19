package is.hi.hbv501g.web;

import is.hi.hbv501g.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import is.hi.hbv501g.domain.Track;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackService trackService;

    public TrackController(TrackService trackService) { this.trackService = trackService; }

    // Fetches and list all tracks (paginated)
    @GetMapping
    public Page<Track> list(@PageableDefault(size = 10) Pageable pageable) {            // 10 = default page size
        return trackService.list(pageable);
    }
}
