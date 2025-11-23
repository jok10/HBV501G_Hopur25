package is.hi.hbv501g.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/debug/session")
    public Map<String, Object> debugSession(HttpSession session) {
        Map<String, Object> out = new HashMap<>();
        out.put("sessionId", session.getId());
        out.put("creationTime", session.getCreationTime());
        out.put("lastAccessedTime", session.getLastAccessedTime());
        out.put("maxInactiveInterval", session.getMaxInactiveInterval());

        // This may be null — HashMap is fine with that.
        out.put("userId", session.getAttribute("userId"));

        return out;
    }
}
