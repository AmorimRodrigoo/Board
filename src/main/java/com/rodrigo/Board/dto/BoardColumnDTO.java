package com.rodrigo.Board.dto;

import com.rodrigo.Board.percistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(
        Long id, String name, BoardColumnKindEnum kind, int cardsAmount
) {
}
