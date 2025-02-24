package com.rodrigo.Board.percistence.migration;

import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;
//liquibase
    private void executeMigration(){

    }
}
