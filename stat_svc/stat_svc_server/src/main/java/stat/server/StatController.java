package stat.server;

import dto.EndpointHitDto;
import dto.ViewStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class StatController {
    private final StatService service;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatController(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("endpoint hit");
        service.hit(endpointHitDto);
        log.info("hit saved");
    }

    @GetMapping("/stats")
    public List<ViewStats> stats(@RequestParam String start, @RequestParam String end, @RequestParam(defaultValue = "") List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("endpoint stats");
        if (LocalDateTime.parse(start, DATE_TIME_FORMAT).isAfter(LocalDateTime.parse(end, DATE_TIME_FORMAT))) {
            throw new IllegalArgumentException("Invalid time range");
        }
        List<ViewStats> viewStats = service.stats(start, end, uris, unique);
        log.info("stats received");
        return viewStats;
    }
}
