package main.server.repository;

import main.server.dao.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT * FROM events WHERE initiator_id = ?1", nativeQuery = true)
    List<Event> findAllByInitiatorId(int userId);
}
