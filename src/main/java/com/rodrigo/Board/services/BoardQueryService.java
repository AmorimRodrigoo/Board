package com.rodrigo.Board.services;


import com.rodrigo.Board.dto.BoardDetailsDTO;
import com.rodrigo.Board.percistence.dao.BoardColumnDAO;
import com.rodrigo.Board.percistence.dao.BoardDAO;
import com.rodrigo.Board.percistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        var optional =  dao.findById(id);
        if (optional.isPresent()) {
            var entity = optional.get();
            entity.setBoardColumns(boardColumnDAO.findByBoardId((entity.getId())));
            return Optional.of(entity);
        }
        return Optional.empty();
    }
    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        var optional =  dao.findById(id);
        if (optional.isPresent()) {
            var entity = optional.get();
            var columns = boardColumnDAO.findByBoardIdWithDetails((entity.getId()));
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(),columns);
            return Optional.of(dto);
        }
        return Optional.empty();
    }


}

