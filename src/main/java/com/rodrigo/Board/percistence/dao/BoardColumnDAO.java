package com.rodrigo.Board.percistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import com.rodrigo.Board.percistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardColumnDAO {

    private Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, order, kind, board_id) VALUES (?, ?, ?, ?)";
        try(var statemant = connection.prepareStatement(sql)) {
            var i = 1;
            statemant.setString(i++, entity.getName());
            statemant.setInt(i++, entity.getOrder());
            statemant.setString(i++, entity.getKind().name());
            statemant.setLong(i++, entity.getBoard().getId());
            statemant.executeUpdate();
            if (statemant instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }
}
