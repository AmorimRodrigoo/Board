package com.rodrigo.Board.dto;

import com.rodrigo.Board.percistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
