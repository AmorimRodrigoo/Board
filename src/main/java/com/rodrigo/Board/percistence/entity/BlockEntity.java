package com.rodrigo.Board.percistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {

    private Long id;
    private OffsetDateTime blocked_At;
    private String block_Reason;
    private OffsetDateTime unblocked_At;
    private String unblock_Reason;


}
