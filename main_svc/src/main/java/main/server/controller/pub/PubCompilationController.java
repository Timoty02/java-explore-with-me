package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/compilations")
public class PubCompilationController {
    @GetMapping
    public String getCompilations(@RequestParam boolean pinned, @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return "getCompilations";
    }

    @GetMapping("/{compId}")
    public String getCompilationById(@PathVariable int compId) {
        return "getCompilationById";
    }
}
