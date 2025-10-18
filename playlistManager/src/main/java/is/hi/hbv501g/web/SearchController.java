package is.hi.hbv501g.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g.service.SearchService;
import is.hi.hbv501g.domain.Track;


@RestController
public class SearchController {
    private final SearchService searchservice;

    public SearchController(SearchService searchservice) { this.searchservice = searchservice; }

    @GetMapping("/api/tracks/search")           // calls this method when someone hits that path
    public Page<Track> search(@RequestParam String query, @PageableDefault(size = 10) Pageable pageable) {          // 10 = default page size

        return searchservice.search(query, pageable);
    }
}
