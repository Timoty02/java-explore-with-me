package main.server.repository;

import main.server.dao.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>{
    List<Compilation> findAllByPinned(Boolean pinned);
}
