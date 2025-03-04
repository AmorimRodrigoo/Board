package com.rodrigo.Board.services;

import com.rodrigo.Board.dto.CardDetailsDTO;
import com.rodrigo.Board.percistence.dao.BoardColumnDAO;
import com.rodrigo.Board.percistence.dao.CardDAO;
import com.rodrigo.Board.percistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.findByID(id);
    }
}
