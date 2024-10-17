package stat.server;

import dto.EndpointHitDto;
import dto.ViewStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class StatController {
    private final StatService service;

    @Autowired
    public StatController(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    public void hit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("endpoint hit");
        service.hit(endpointHitDto);
        log.info("hit saved");
    }

    @GetMapping("/stats")
    public List<ViewStats> stats(@RequestParam String start, @RequestParam String end, @RequestParam(defaultValue = "") List<String> uris, @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("endpoint stats");
        List<ViewStats> viewStats = service.stats(start, end, uris, unique);
        log.info("stats received");
        return viewStats;
    }
}
