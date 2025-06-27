package org.vazhmax.trellish.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vazhmax.trellish.api.dto.TaskStateDto;
import org.vazhmax.trellish.service.TaskStateService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public final class TaskStateController {
    private static final String GET_BY_ID = "/api/board/{boardId}/task_state/{taskStateId}";
    private static final String GET_BY_BOARD_ID = "/api/board/{boardId}/task_state";
    private static final String CREATE_TASK_STATE = "/api/board/{boardId}/task_state";
    private static final String EDIT_TASK_STATE = "/api/board/{boardId}/task_state/{taskStateId}";
    private static final String MOVE_TASK_STATE = "/api/board/{boardId}/task_state/{taskStateId}/move";

    private final TaskStateService taskStateService;

    @GetMapping(GET_BY_ID)
    public TaskStateDto getById(@PathVariable Integer boardId, @PathVariable Integer taskStateId) {
        return taskStateService.getById(taskStateId);
    }

    @GetMapping(GET_BY_BOARD_ID)
    public List<TaskStateDto> getByBoardId(@PathVariable Integer boardId) {
        return taskStateService.getByBoardId(boardId);
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto create(@RequestParam String name, @PathVariable Integer boardId) {
        return taskStateService.create(boardId, name);
    }

    @PutMapping(EDIT_TASK_STATE)
    public TaskStateDto changeName(@PathVariable("boardId") Integer ignored, @PathVariable Integer taskStateId,
                                   @RequestParam String name) {
        return taskStateService.changeName(taskStateId, name);
    }

    @PutMapping(MOVE_TASK_STATE)
    public TaskStateDto moveTaskState(@PathVariable Integer boardId, @PathVariable Integer taskStateId,
                                      @RequestParam String leftRank, @RequestParam String rightRank) {
        return taskStateService.move(boardId, taskStateId, leftRank, rightRank);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        return;
    }
}
