package main.server.repository;

import main.server.dao.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>{
}
