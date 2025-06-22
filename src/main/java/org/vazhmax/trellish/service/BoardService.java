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
        return db.selectFrom(BOARD).where(BOARD.IS_DELETED.eq(false)).fetch()
                .stream().map(boardMapper::toDto).toList();
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

        db.update(BOARD).set(BOARD.NAME, name).where(BOARD.ID.eq(id)).and(BOARD.IS_DELETED.eq(false)).execute();

        return getById(id);
    }

    public void delete(Integer id){
        getRecordById(id);
        db.update(BOARD).set(BOARD.IS_DELETED, true).where(BOARD.ID.eq(id)).execute();
    }

    private void checkNameTaken(String name){
        db.fetchOptional(BOARD, BOARD.NAME.eq(name), BOARD.IS_DELETED.eq(false))
                .ifPresent(s -> {
                    throw new BadRequestException(
                            String.format("Board with name %s already exists: %s", s.getName(), s.getId()));
                });
    }


    private BoardRecord getRecordById(Integer id){
        return db.fetchOptional(BOARD, BOARD.ID.eq(id), BOARD.IS_DELETED.eq(false))
                .orElseThrow(() -> new NotFoundException(String.format("Board with id %s not found", id)));
    }
}
