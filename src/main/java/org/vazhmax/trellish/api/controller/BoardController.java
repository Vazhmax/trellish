package org.vazhmax.trellish.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vazhmax.trellish.api.dto.BoardDto;
import org.vazhmax.trellish.api.dto.TaskStateDto;
import org.vazhmax.trellish.service.BoardService;
import org.vazhmax.trellish.service.TaskStateService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public final class BoardController {
    private static final String FETCH_BOARDS = "/api/board";
    private static final String CREATE_BOARD = "/api/board";
    private static final String GET_BY_ID = "/api/board/{id}";
    private static final String EDIT_BOARD = "/api/board/{id}";
    private static final String DELETE_BOARD = "/api/board/{id}";

    private final BoardService boardService;
    private final TaskStateService taskStateService;

    @GetMapping(FETCH_BOARDS)
    public List<BoardDto> getAll() {
        return boardService.getAll();
    }

    @GetMapping(GET_BY_ID)
    public BoardDto getById(@PathVariable Integer id) {
        var board = boardService.getById(id);

        List<TaskStateDto> taskStates = taskStateService.getByBoardId(id);
        board.setTaskStates(taskStates);

        return board;
    }

    @PostMapping(CREATE_BOARD)
    public BoardDto create(@RequestParam String name) {
        return boardService.create(name);
    }

    @PutMapping(EDIT_BOARD)
    public BoardDto changeName(@PathVariable Integer id, @RequestParam String name) {
        return boardService.changeName(id, name);
    }

    @DeleteMapping(DELETE_BOARD)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        boardService.delete(id);
    }
}
