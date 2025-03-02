package com.rodrigo.Board.ui;

import com.rodrigo.Board.percistence.entity.BoardColumnEntity;
import com.rodrigo.Board.percistence.entity.BoardColumnKindEnum;
import com.rodrigo.Board.percistence.entity.BoardEntity;
import com.rodrigo.Board.services.BoardQueryService;
import com.rodrigo.Board.services.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.rodrigo.Board.percistence.config.ConnectionConfig.getConnection;
import static com.rodrigo.Board.percistence.entity.BoardColumnKindEnum.*;


public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException{
        System.out.println("Bem vindo ao Boards, escolha uma das opções");
        var option = -1;
        while (true) {
            System.out.println("1  - Criar novo Board");
            System.out.println("2 - Selecionar um Board");
            System.out.println("3 - Excluir um Board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, tente novamente");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();
        System.out.println("Digite o nome do board");
        entity.setName(scanner.next());


        System.out.println(
                "Seu board terá mais que 3 colunas? Se sim, informe quantas, se não digite 0");
        var additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, PENDING, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);
        try(var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);

        }


    }

    private void selectBoard() throws SQLException {
        System.out.println("Digite o id do board");
        var id = scanner.nextLong();
        try(var connection = getConnection()){
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrado Board com esse id %d\n", id)
            );
        }

    }

    private void deleteBoard() throws SQLException {
        System.out.println("Digite o id do board");
        var id = scanner.nextLong();
        try(var connection = getConnection() ) {
            var service = new BoardService(connection);
            if(service.delete(id)){
                System.out.printf("Board %s deletado com sucesso\n", id);
            }else {
                System.out.printf("Erro ao deletar o board %d\n", id);
            }
        }
    }

    private BoardColumnEntity createColumn(
            final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}
