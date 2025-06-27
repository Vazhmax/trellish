package org.vazhmax.trellish.api.mapper;

import org.springframework.stereotype.Component;
import org.vazhmax.trellish.api.dto.TaskStateDto;
import ru.vazhmax.trellish.tables.records.TaskStateRecord;

@Component
public final class TaskStateMapper {

    public TaskStateDto toDto(TaskStateRecord taskState) {
        return TaskStateDto.builder()
                .id(taskState.getId())
                .name(taskState.getName())
                .lexorank(taskState.getLexorank())
                .boardId(taskState.getBoardId())
                .build();
    }
}
