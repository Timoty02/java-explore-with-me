package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Compilation;
import main.server.dao.Event;
import main.server.dto.CompilationDto;
import main.server.dto.NewCompilationDto;
import main.server.dto.UpdateCompilationRequest;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
import main.server.mapper.CompilationMapper;
import main.server.repository.CompilationRepository;
import main.server.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    public void deleteCompilationAdmin(int compId) {
        log.info("Deleting compilation with id: {}", compId);
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.deleteById(compId);
        log.info("Compilation deleted");
    }

    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
        log.info("Adding new compilation: {}", newCompilationDto);
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        try {
            Compilation compilationUp = compilationRepository.save(compilation);
            log.info("Compilation added: {}", compilationUp);
            return CompilationMapper.toCompilationDto(compilationUp);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public CompilationDto updateCompilationAdmin(int compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Updating compilation with id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation with id=" + compId + " was not found"));
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        Compilation compilationUp = compilationRepository.save(compilation);
        log.info("Compilation updated: {}", compilationUp);
        return CompilationMapper.toCompilationDto(compilationUp);
    }

    public List<CompilationDto> getCompilationsPub(Boolean pinned, Integer from, Integer size) {
        log.info("Getting compilations");
        List<Compilation> compilations;
        if (pinned != null && pinned) {
            compilations = compilationRepository.findAllByPinned(pinned).stream().toList();
        } else {
            compilations = compilationRepository.findAll().stream().toList();
        }
        if (from != null && size != null) {
            compilations = compilations.stream().skip(from).limit(size).toList();
        }
        log.info("Found compilations: {}", compilations);
        return compilations.stream().map(CompilationMapper::toCompilationDto).toList();
    }

    public CompilationDto getCompilationByIdPub(int compId) {
        log.info("Getting compilation with id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Compilation with id=" + compId + " was not found"));
        log.info("Found compilation: {}", compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }
}
