package stat.server;

import dto.EndpointHitDto;
import dto.ViewStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StatService {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatRepository repository;
    @Autowired
    public StatService(StatRepository repository){
        this.repository = repository;
    }
    public void hit(EndpointHitDto endpointHitDto) {
        log.info("hit: {}", endpointHitDto);
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        repository.save(endpointHit);
        log.info("hit saved: {}", endpointHit);
    }

    public List<ViewStats> stats(String start, String end, List<String> uris, Boolean unique) {
        log.info("getting stats");
        List<ViewStats> viewStats = new ArrayList<>();
        if (uris == null ||uris.isEmpty()) {
            uris = repository.findDistinctUris();
        }
        for (String uri:uris) {
            ViewStats viewStat = new ViewStats();
            viewStat.setUri(uri);
            viewStat.setApp(repository.findFirstAppByUri(uri));
            Integer hits;
            if (unique){
                hits = repository.countByUriAndTimestampBetweenDistinctIp(uri, LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter));
            } else {
                hits = repository.countByUriAndTimestampBetween(uri, LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter));
            }
            viewStat.setHits(hits);
            viewStats.add(viewStat);
        }
        return viewStats;
    }
}
