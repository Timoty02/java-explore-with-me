package stat.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT DISTINCT e.uri FROM EndpointHit e")
    List<String> findDistinctUris();
    @Query("SELECT COUNT (DISTINCT e.ip) FROM EndpointHit e WHERE e.uri = ?1")
    Integer countByUriDistinctIp(String uri);
    @Query("SELECT COUNT (e) FROM EndpointHit e WHERE e.uri = ?1")
    Integer countByUri(String uri);
    @Query("SELECT COUNT (e) FROM EndpointHit e WHERE e.uri = ?1 AND e.timestamp BETWEEN ?2 AND ?3")
    Integer countByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT (DISTINCT e.ip) FROM EndpointHit e WHERE e.uri = ?1 AND e.timestamp BETWEEN ?2 AND ?3")
    Integer countByUriAndTimestampBetweenDistinctIp(String uri, LocalDateTime start, LocalDateTime end);
    @Query(value = "SELECT app FROM endpoint_hits WHERE uri = ?1 LIMIT 1", nativeQuery = true)
    String findFirstAppByUri(String uri);
}
