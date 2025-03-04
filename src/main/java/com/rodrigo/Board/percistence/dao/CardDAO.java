package com.rodrigo.Board.percistence.dao;

import com.rodrigo.Board.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


import static com.rodrigo.Board.percistence.converter.OffSetDateTimeConverter.toOffsetDateTime;


@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public Optional<CardDetailsDTO> findByID(final Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.blocked_reason,
                       c.board_column_id,
                       bc.name,
                       COUNT(
                       SELECT sub_b.id FROM BLOCKS sub_b WHERE sub_b.card_id = c.id
                       ) blocks_amount
                FROM CARDS c
                LEFT JOIN BLOCKS b
                ON c.id = b.card_id
                AND b.unblocked_at IS NULL
                INNER BOARD_COLUMNS bc
                ON bc.id = c.board_column_id
                WHERE id = ?
                """;
        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("c.id"),
                        resultSet.getString("c.title"),
                        resultSet.getString("c.description"),
                        resultSet.getString("b.blocked_reason").isEmpty(),
                        toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                        resultSet.getString("b.blocked_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("c.board_column_id"),
                        resultSet.getString("bc.name")


                );
                return Optional.of(dto);
            }
        }

        return Optional.empty();
    }
}
