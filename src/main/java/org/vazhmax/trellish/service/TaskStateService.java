package org.vazhmax.trellish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.vazhmax.trellish.api.dto.TaskStateDto;
import org.vazhmax.trellish.api.exception.NotFoundException;
import org.vazhmax.trellish.api.mapper.TaskStateMapper;
import ru.vazhmax.trellish.tables.records.TaskStateRecord;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.max;
import static ru.vazhmax.trellish.tables.TaskState.TASK_STATE;

@Slf4j
@RequiredArgsConstructor
@Service
public final class TaskStateService {
    private final DSLContext db;
    private final TaskStateMapper taskStateMapper;
    private final BoardService boardService;
    private final LexorankService lexorankService;

    private static final String LEFT_NULL = "aaa";
    private static final String RIGHT_NULL = "zzz";

    public List<TaskStateDto> getByBoardId(Integer boardId) {
        boardService.getById(boardId);

        return db.fetchStream(TASK_STATE, TASK_STATE.IS_DELETED.isFalse(), TASK_STATE.BOARD_ID.eq(boardId))
                .map(taskStateMapper::toDto).sorted(Comparator.comparing(TaskStateDto::getLexorank)).toList();
    }

    public TaskStateDto getById(Integer id) {
        return taskStateMapper.toDto(getRecordById(id));
    }

    public TaskStateDto changeName(Integer id, String name) {
        var taskState = getRecordById(id);

        taskState.setName(name);
        taskState.store();

        return taskStateMapper.toDto(taskState);
    }

    public TaskStateDto create(Integer boardId, String name) {
        boardService.getById(boardId);

        String left = getLastLexorank(boardId).orElse(LEFT_NULL);
        String lexorank = lexorankService.findMedian(left, RIGHT_NULL);

        var taskState = db.newRecord(TASK_STATE);
        taskState.setName(name);
        taskState.setBoardId(boardId);
        taskState.setIsDeleted(false);
        taskState.setLexorank(lexorank);
        taskState.store();

        return taskStateMapper.toDto(taskState);
    }

    public TaskStateDto move(Integer boardId, Integer taskStateId, String firstRank, String secondRank) {
        //TODO check all the taskStates are on the same board
        TaskStateRecord taskState = getRecordById(taskStateId);

        String lexorank = lexorankService.findMedian(firstRank, secondRank);

        taskState.setLexorank(lexorank);
        taskState.store();

        return taskStateMapper.toDto(taskState);
    }

    private Optional<String> getLastLexorank(Integer boardId) {
        var ID = TASK_STATE.ID;
        return db.select(max(TASK_STATE.LEXORANK), ID).from(TASK_STATE)
                .where(ID.eq(boardId)).groupBy(ID)
                .fetchOptional().map(s -> s.get(max(TASK_STATE.LEXORANK)));
    }

    private TaskStateRecord getRecordById(Integer id) {
        return db.fetchOptional(TASK_STATE, TASK_STATE.ID.eq(id), TASK_STATE.IS_DELETED.isFalse())
                .orElseThrow(() -> new NotFoundException(String.format("Task State with id %s not found", id)));
    }

    //select max(lexorank), board_id from prod.task_state where board_id = 1 group by board_id;
}
