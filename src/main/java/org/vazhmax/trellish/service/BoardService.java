package org.vazhmax.trellish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.vazhmax.trellish.api.dto.BoardDto;
import org.vazhmax.trellish.api.exception.BadRequestException;
import org.vazhmax.trellish.api.exception.NotFoundException;
import org.vazhmax.trellish.api.mapper.BoardMapper;
import ru.vazhmax.trellish.tables.records.BoardRecord;

import java.util.List;

import static ru.vazhmax.trellish.Tables.BOARD;

@RequiredArgsConstructor
@Slf4j
@Service
public final class BoardService {
    private final DSLContext db;
    private final BoardMapper boardMapper;

    public List<BoardDto> getAll() {
        return db.fetch(BOARD, BOARD.IS_DELETED.isFalse()).map(boardMapper::toDto);
    }

    public BoardDto getById(Integer id) {
        return boardMapper.toDto(getRecordById(id));
    }

    public BoardDto create(String name) {
        checkNameTaken(name);

        var board = db.newRecord(BOARD);
        board.setName(name);
        board.setIsDeleted(false);
        board.store();

        return boardMapper.toDto(board);
    }

    public BoardDto changeName(Integer id, String name) {
        checkNameTaken(name);

        var board = getRecordById(id);
        board.setName(name);
        board.store();

        return boardMapper.toDto(board);
    }

    public void delete(Integer id){
        var board = getRecordById(id);

        board.setIsDeleted(true);
        board.store();
    }

    private void checkNameTaken(String name){
        db.fetchOptional(BOARD, BOARD.NAME.eq(name), BOARD.IS_DELETED.isFalse())
                .ifPresent(s -> {
                    throw new BadRequestException(
                            String.format("Board with name %s already exists: %s", s.getName(), s.getId()));
                });
    }


    private BoardRecord getRecordById(Integer id){
        return db.fetchOptional(BOARD, BOARD.ID.eq(id), BOARD.IS_DELETED.isFalse())
                .orElseThrow(() -> new NotFoundException(String.format("Board with id %s not found", id)));
    }
}
