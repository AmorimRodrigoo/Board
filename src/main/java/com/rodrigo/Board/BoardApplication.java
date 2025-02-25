package com.rodrigo.Board;

import com.rodrigo.Board.percistence.migration.MigrationStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

import static com.rodrigo.Board.percistence.config.ConnectionConfig.getConnection;


@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(BoardApplication.class, args);
		try(var connection = getConnection()){
			new MigrationStrategy(connection).executeMigration();
		}
	}

}
