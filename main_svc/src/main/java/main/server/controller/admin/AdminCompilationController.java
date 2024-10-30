package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.CompilationDto;
import main.server.dto.NewCompilationDto;
import main.server.dto.UpdateCompilationRequest;
import main.server.service.CompilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("createCompilation:{}", newCompilationDto);
        newCompilationDto.validate();
        CompilationDto compilationDto = compilationService.addCompilationAdmin(newCompilationDto);
        log.info("createdCompilation:{}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compId) {
        log.info("deleteCompilation");
        compilationService.deleteCompilationAdmin(compId);
        log.info("deletedCompilation");
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId, @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("updateCompilation:{}", updateCompilationRequest);
        updateCompilationRequest.validate();
        CompilationDto compilationDto = compilationService.updateCompilationAdmin(compId, updateCompilationRequest);
        log.info("updatedCompilation:{}", compilationDto);
        return compilationDto;
    }
}
