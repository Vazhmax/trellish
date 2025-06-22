package org.vazhmax.trellish.api.mapper;

import org.springframework.stereotype.Component;
import org.vazhmax.trellish.api.dto.BoardDto;
import ru.vazhmax.trellish.tables.records.BoardRecord;

@Component
public final class BoardMapper {
    public BoardDto toDto(BoardRecord board){
        return BoardDto.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }
}
