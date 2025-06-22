package org.vazhmax.trellish.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vazhmax.trellish.api.dto.BoardDto;
import org.vazhmax.trellish.service.BoardService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public final class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/board")
    public List<BoardDto> getAll() {
        return boardService.getAll();
    }

    @GetMapping("/api/board/{id}")
    public BoardDto getById(@PathVariable Integer id) {
        return boardService.getById(id);
    }

    @PostMapping("/api/board")
    public BoardDto create(@RequestParam String name) {
        return boardService.create(name);
    }

    @PutMapping("/api/board/{id}")
    public BoardDto changeName(@PathVariable Integer id, @RequestParam String name) {
        return boardService.changeName(id, name);
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        boardService.delete(id);
    }
}
