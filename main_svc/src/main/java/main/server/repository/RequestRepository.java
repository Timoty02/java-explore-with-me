package main.server.repository;

import main.server.dao.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.event.id = ?1")
    List<Request> findAllByEventId(int eventId);

    @Query("select r from Request r where r.requester.id = ?1")
    List<Request> findAllByRequesterId(int userId);

    boolean existsByEventIdAndRequesterId(int eventId, int userId);
}
