
/* Gomoku or also known as five in a row is a classic game from Japan.
For each game, there are 2 players who take turns to place a stone.
The winner of this game is the fastest player to get 5 stones/ dots in a row
(can be diagonal, horizontal or vertical).

Player 1 has a black dot and is the first player to move, whereas Player 2 is indicated by white dot.
The board size of this game is 15x15. Each player can only place a stone on each line crossings
and are not allowed to place it outside of the board.

This file is in charged for the logic / algorithm of the game,
e.g. check winning condition and check whether move is valid.
*/

package org.example.javafx;

class GomokuGame {
    private int[][] board;          // 0: empty, 1: player1's stone, 2: player2's stone
    private int currentPlayer;      // 1: player1, 2: player2
    private int moves1;             // number of moves by player 1
    private  int moves2;            // number of moves by player 2
    private boolean gameOver;       // true: game over, false: game not over
    private int winner;             // 0: no winner / tie, 1: player 1 wins, 2: player 2 wins
    private int boardSize;          // size of the board (number of columns / rows)

    // Constructor for Gomoku game with a specific board size
    public GomokuGame(int boardSize) {
        if (boardSize < 5 || boardSize > 20) {
            throw new IllegalArgumentException("Board size should be between 5 and 20.");
        }
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];            // Default int : all zeros (empty)
        currentPlayer = 1;
        gameOver = false;
        winner = 0; //no winner
    }

    // Default constructor for Gomoku game
    public GomokuGame() { this(15); }

    // Check win condition, whether there are 5 stones in a row for all directions
    public boolean checkWin(int x, int y) {
        int[][][] directionLines = {{{0, 1}, {0, -1}},  // vertical
                {{1, 0}, {-1, 0}},                // horizontal
                {{1, 1}, {-1, -1}},               // diagonal
                {{1, -1}, {-1, 1}}};              // anti-diagonal
        for (int[][] oppositeDirs : directionLines) {
            int count = 1;
            for (int[] direction: oppositeDirs) {
                int dx = direction[0];
                int dy = direction[1];
                for (int i = 1; i < 5; i++) {
                    int newX = x + i * dx;
                    int newY = y + i * dy;

                    // If the newX and newY is invalid (out of board) or
                    // the neighbour stone is of different color, then stop checking in that direction
                    if (!isValidPosition(newX, newY) || board[newX][newY] != board[x][y]) {
                        break;
                    }
                    count++;

                    // If there is 5 or more stones with same color put beside each other, then there is a winner
                    if (count >= 5) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // To place a piece at (x, y) for the current player, and then switch to the other player
    public boolean move(int x, int y) {

        // Some conditions to make sure that the player can move/ place a stone:
        // if the game is over, then cannot move
        // if the move is placed outside of board
        // if the position of move is already occupied
        if (gameOver) { return false; }
        if (!isValidPosition(x, y)) { return false; }
        if (board[x][y] != 0) { return false;}

        // Place the stone and check whether it leads to a game over
        board[x][y] = currentPlayer;
        if (checkWin(x, y)) {
            gameOver = true;
            winner = currentPlayer;
        }

        // If the move is valid, check who is the current player
        // Add the current player's number of move and switch to the other player
        if (currentPlayer==1){
            moves1++;
            currentPlayer = 2;
        }
        else{
            moves2++;
            currentPlayer = 1;
        }

        Integer totMoves = totalMoves();

        // To check draw condition by comparing the maximum number of possible moves
        // and the total of moves that have been made
        if (totMoves.equals(boardSize*boardSize)){
            gameOver = true;
            winner = 0;
        }
        return true;
    }

    // To get the current player; 1: player1, 2: player2
    public int getCurrentPlayer(){ return currentPlayer; }

    // To get the number of moves by player 1
    public int getMoves1(){ return moves1; }

    // To get the number of moves by player 2
    public int getMoves2(){ return moves2; }

    // To get the number of total moves by player 1 and 2
    public int totalMoves(){ return moves1 + moves2; }

    // Check whether the position is still in the scope of the board (valid) or not
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    // To get the bool whether the game is over
    public boolean isGameOver() { return gameOver; }

    // To get the winner of the game 0 = tie, 1 = player 1, 2 = player 2
    public int getWinner() { return winner; }

    // To get the current board (which show which spot is empty, which is occupied and by which stone)
    public int[][] getBoard() { return board; }
}