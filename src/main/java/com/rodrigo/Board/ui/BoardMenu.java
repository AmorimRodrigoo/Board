package com.rodrigo.Board.ui;

import com.rodrigo.Board.percistence.entity.BoardColumnEntity;
import com.rodrigo.Board.percistence.entity.BoardEntity;
import com.rodrigo.Board.services.BoardColumnQueryService;
import com.rodrigo.Board.services.BoardQueryService;
import com.rodrigo.Board.services.CardQueryService;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static com.rodrigo.Board.percistence.config.ConnectionConfig.getConnection;


@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao menu %s, selecione uma das opções", entity.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear  um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Vizualizar board");
                System.out.println("7 - Vizualizar colunas com cards");
                System.out.println("8 - Vizualizar cards");
                System.out.println("9 - Voltar para Menu Principal");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> MoveCardToNextColumn();
                    case 3 -> BlockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando ao menu principal");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, tente novamente");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() {
    }

    private void MoveCardToNextColumn() {
    }

    private void BlockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b ->{
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                   System.out.printf("Column [%s] tipo: [%s] tem %s cards\n",
                           c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumn = -1L;
        while (!columnsIds.contains(selectedColumn)){
            System.out.printf("Escolha uma coluna do board %s\n", entity.getName());
            entity.getBoardColumns().forEach(
                    c ->System.out.printf("%s - %s [%s]\n",
                            c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }
        try(var connection = getConnection()){
            var column = new BoardColumnQueryService(connection).findById(selectedColumn);
            column.ifPresent(co ->{
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(
                        ca ->System.out.printf("Card %s - %s\nDescrição: %s",
                                ca.getId(), ca.getTitle(), ca.getDescription()));
            } );
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o ID do Card");
        var selectedCardId = scanner.nextLong();
        try(var connection = getConnection()){
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s\n", c.id(), c.title());
                                System.out.printf("Descrição %s\n", c.description());
                                System.out.println(c.blocked() ?
                                        "Está bloqueado, motivo: %s" + c.blockReason() :
                                        "Não está blouqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blockAmount());
                                System.out.printf("está no momento na coluna %s - %s",
                                        c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com o id %s", selectedCardId));
        }
    }
}
