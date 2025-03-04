package com.rodrigo.Board.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,
        String title,
        String description,
        boolean blocked,
        OffsetDateTime blockedAt,
        String blockReason,
        int blockAmount,
        Long columnId,
        String columnName
) {
}
