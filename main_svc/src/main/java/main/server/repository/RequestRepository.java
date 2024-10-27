package main.server.repository;

import main.server.dao.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
