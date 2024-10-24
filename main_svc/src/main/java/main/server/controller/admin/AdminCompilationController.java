package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.NewCompilationDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    @PostMapping
    public String createCompilation(@RequestBody NewCompilationDto  newCompilationDto) {
        log.info("createCompilation");
        return "createCompilation";
    }
}
