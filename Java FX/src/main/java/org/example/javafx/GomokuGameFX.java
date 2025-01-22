
/* Gomoku or also known as five in a row is a classic game from Japan.
For each game, there are 2 players who take turns to place a stone.
The winner of this game is the fastest player to get 5 stones/ dots in a row
(can be diagonal, horizontal or vertical).

Player 1 has a black dot and is the first player to move, whereas Player 2 is indicated by white dot.
The board size of this game is 15x15. Each player can only place a stone on each line crossings
and are not allowed to place it outside of the board.

This file is in charge of the visual or interface of the game.
It provides a user-friendly interface, by showing whose turn it is,
the number of moves of each player, and the result of game (who won / draw).
*/

package org.example.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GomokuGameFX extends Application {
    private static final int cellSize = 40; // size of each cell (pixels)
    private static final int boardSize = 15; // number of rows/ columns
    private GomokuGame game;

    @Override
    public void start(Stage primaryStage) {
        game = new GomokuGame(boardSize);
        GridPane outerRoot = new GridPane(); // the big background panel
        VBox rightPanel = new VBox();   // panel for top right corner
        HBox bottomPanel = new HBox(); //panel for bottom right corner
        Canvas canvas = new Canvas(cellSize * boardSize, cellSize * boardSize); // panel for game board

        Text boardSizeText = new Text("Board size: " + boardSize + "x" + boardSize);
        Text currentPlayer= new Text();
        Text movePlayer1 = new Text();
        Text movePlayer2 = new Text();
        Text totalMoves = new Text();
        Text  message = new Text();

        Button reset= new Button("Reset");
        Button exit= new Button("Exit");

        // Set every visual component and add it to the big background panel
        resetText(currentPlayer, movePlayer1, movePlayer2, totalMoves, message, game);
        outerRoot.getChildren().add(canvas);
        drawBoard(canvas.getGraphicsContext2D());

        // Reset button : if pressed, the game restarts from the beginning
        reset.setOnAction(e-> {
            game = new GomokuGame(boardSize);
            drawBoard(canvas.getGraphicsContext2D());
            resetText(currentPlayer, movePlayer1, movePlayer2, totalMoves, message, game);
                });

        // Exit button : if pressed, the game is closed
        exit.setOnAction(e-> {
            primaryStage.close();
        });

        bottomPanel.getChildren().addAll(reset, exit);
        bottomPanel.setSpacing(20);

        Insets insetRight = new Insets(30, 10, 10,10);
        rightPanel.getChildren().addAll(boardSizeText, currentPlayer, movePlayer1, movePlayer2, totalMoves, message);
        rightPanel.setPadding(insetRight); //
        rightPanel.setSpacing(5);

        // For every mouse clicked/ move, update the graphics,
        // ensuring the player can easily navigate the progress of game
        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / cellSize);
            int y = (int) (e.getY() / cellSize);

            if (game.move(x, y)) {
                // For every move made, update the board with the new move
                drawBoard(canvas.getGraphicsContext2D());
                message.setText("Game is in progress...");
            } else {
                message.setText("Invalid move! ");
            }

            // Update the text information on top right panel
            currentPlayer.setText("Current player: " + game.getCurrentPlayer());
            movePlayer1.setText("Player 1 moves: " + game.getMoves1());
            movePlayer2.setText("Player 2 moves: " + game.getMoves2());
            totalMoves.setText("Total moves: " + game.totalMoves());

            // If the game is over, announce who is the winner / the outcome of game
            if (game.isGameOver()) { // If game is over
                Integer winner = game.getWinner();
                if (winner.equals(0)) {
                    message.setText("Game over! Draw! No winner!");
                } else {
                    message.setText("Game over! The winner is player " + game.getWinner() + "!");
                }
            }
        });

        outerRoot.add(rightPanel, 1, 0);
        outerRoot.add(bottomPanel, 1,1);

        Scene scene = new Scene(outerRoot, 800, 650);
        primaryStage.setTitle("Gomoku Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Draw board function by lines
    private void drawBoard(GraphicsContext gc) {
        // draw board (lines) such that the stone are placed in the intersections of lines
        gc.clearRect(0, 0,cellSize * boardSize, cellSize * boardSize);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < boardSize; i++) {
            gc.strokeLine((i * cellSize)+20, 20, (i * cellSize)+20, (cellSize * boardSize)-20);
            gc.strokeLine(20, (i * cellSize)+20, (cellSize * boardSize)-20, (i * cellSize)+20);
        }
        int[][] board = game.getBoard();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                double x = (i + 0.1) * cellSize;
                double y = (j + 0.1) * cellSize;
                double w = cellSize * 0.8;
                double h = cellSize * 0.8;
                //draw black stones if player1 moves, white stones if player2 moves
                if (board[i][j] == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x, y, w, h);
                    gc.strokeOval(x, y, w, h);
                } else if (board[i][j] == 2) {
                    gc.setFill(Color.WHITE);
                    gc.fillOval(x, y, w, h);
                    gc.strokeOval(x, y, w, h);
                }
            }
        }
    }

    // Show texts for initial condition
    private static void resetText(Text currentPlayer, Text movePlayer1, Text movePlayer2, Text totalMoves, Text message, GomokuGame game){
        currentPlayer.setText("Current player: " + game.getCurrentPlayer());
        movePlayer1.setText("Player 1 moves: " + game.getMoves1());
        movePlayer2.setText("Player 2 moves: " + game.getMoves2());
        totalMoves.setText("Total moves: " + game.totalMoves());
        message.setText("Game has not started");
    }

    public static void main(String[] args) {
        launch(args);
    }
}