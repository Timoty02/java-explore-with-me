package main.server.mapper;

import main.server.dao.Compilation;
import main.server.dto.CompilationDto;
import main.server.dto.NewCompilationDto;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(
                Optional.ofNullable(compilation.getEvents()).map(events -> events.stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
        );
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }
}
