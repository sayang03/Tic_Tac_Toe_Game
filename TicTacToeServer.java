import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeServer {
    private ServerSocket serverSocket;
    private Socket player1Socket;
    private Socket player2Socket;
    private PrintWriter player1Out;
    private PrintWriter player2Out;
    private BufferedReader player1In;
    private BufferedReader player2In;
    private char[][] board;
    private boolean player1Turn;

    public TicTacToeServer() throws IOException {
        serverSocket = new ServerSocket(5909);
        System.out.println("Tic Tac Toe Server is running...");

        player1Socket = serverSocket.accept();
        System.out.println("Player 1 connected");
        player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
        player1Out.println("You are player 1. You play as X.");
        player1Out.println("Waiting for player 2.....");

        player2Socket = serverSocket.accept();
        System.out.println("Player 2 connected");
        player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
        player2Out.println("You are player 2. You play as O.");

        player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
        player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));

        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        player1Turn = true;

        player1Out.println("Here is the initial board:");
        printBoard(player1Out);

        player2Out.println("Here is the initial board:");
        printBoard(player2Out);

        while (true) {
            if (player1Turn) {
                player1Out.println("It's your turn. Enter your move (row(0-2) and column(0-2) separated by space):");
                player2Out.println("Waiting for player 1's turn.");
                String move = player1In.readLine();
                int row = Integer.parseInt(String.valueOf(move.charAt(0)));
                int col = Integer.parseInt(String.valueOf(move.charAt(1)));
                try {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'X';
                        player1Out.println("Here is the updated board:");
                        printBoard(player1Out);
                        player2Out.println("Here is the updated board:");
                        printBoard(player2Out);
                        if (checkWin('X')) {
                            player1Out.println("You win!");
                            player2Out.println("You lose!");
                            break;
                        }
                        player1Turn = false;
                    } else {
                        player1Out.println("Invalid move. Try again.");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    player1Out.println("Invalid move. Try again.");
                }
            } else {
                player2Out.println("It's your turn. Enter your move (row(0-2) and column(0-2) separated by space):");
                player1Out.println("Waiting for player 2's turn");
                String move = player2In.readLine();
                int row = Integer.parseInt(String.valueOf(move.charAt(0)));
                int col = Integer.parseInt(String.valueOf(move.charAt(1)));
                try {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'O';
                        player1Out.println("Here is the updated board:");
                        printBoard(player1Out);
                        player2Out.println("Here is the updated board:");
                        printBoard(player2Out);
                        if (checkWin('O')) {
                            player2Out.println("You win!");
                            player1Out.println("You lose!");
                            break;
                        }
                        player1Turn = true;
                    } else {
                        player2Out.println("Invalid move. Try again.");
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    player2Out.println("Invalid move. Try again.");
                }
            }
        }
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the client socket
            try {
                if (player1Socket != null && !player1Socket.isClosed() && player2Socket != null && !player2Socket.isClosed()) {
                    player1Socket.close();
                    player2Socket.close();
                    System.out.println("Client socket closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkWin(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        // Check diagonals
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private void printBoard(PrintWriter out) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                out.print(board[i][j]);
                if (j < 2) {
                    out.print('|');
                }
            }
            out.println();
            if (i < 2) {
                out.println("-+-+-");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TicTacToeServer server = new TicTacToeServer();
    }
}