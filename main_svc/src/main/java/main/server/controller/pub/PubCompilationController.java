package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CompilationDto;
import main.server.service.CompilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
public class PubCompilationController {
    private final CompilationService compilationService;
    @Autowired
    public PubCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam Boolean pinned, @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Getting compilations with pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationService.getCompilationsPub(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable int compId) {
        log.info("Getting compilation by id: {}", compId);
        CompilationDto compilationDto = compilationService.getCompilationByIdPub(compId);
        log.info("Compilation found: {}", compilationDto);
        return compilationDto;
    }
}
