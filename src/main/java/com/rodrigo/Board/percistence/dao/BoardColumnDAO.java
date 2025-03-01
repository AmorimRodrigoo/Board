package com.rodrigo.Board.percistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import com.rodrigo.Board.percistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.rodrigo.Board.percistence.entity.BoardColumnKindEnum.findByName;

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

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException{
        var sql = "SELECT id, name, `order´ FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY ´order´";
        List<BoardColumnEntity> entities = new ArrayList<>();
        try (var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(findByName(resultSet.getString("kind")));
                entities.add(entity);
            }
        }
        return null;
    }
}
