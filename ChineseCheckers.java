import java.util.Random;
import java.util.Scanner;

public class ChineseCheckers {
    private static final int MAX_BOARD_SIZE = 8;
    static int boardSize;
    private static final String PLAYER_PIECE = "X";
    private static final String AI_PIECE = "O";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter 1 to play against an AI opponent, or 0 to play against another player:");
        int playAgainstAI = input.nextInt();

        System.out.println("Enter the size of the board (maximum " + MAX_BOARD_SIZE + "):");
        int boardSizee = input.nextInt();
        if (boardSizee > MAX_BOARD_SIZE) {
            boardSizee = MAX_BOARD_SIZE;
        }

        boardSize = boardSizee;

        String[][] board = new String[boardSizee][boardSizee];
        boolean playerTurn = true;

        initBoard(board);

        placePieces(board, boardSizee);

        printBoard(board);

        while (true) {
            if (playerTurn) {
                int[] move = getPlayerMove(input, board, PLAYER_PIECE);
                int row = move[0];
                int col = move[1];

                board[row][col] = PLAYER_PIECE;
                playerTurn = false;
            } else {
                if (playAgainstAI == 1) {
                    int[] move = getAIMove(board, AI_PIECE);
                    int row = move[0];
                    int col = move[1];
                    board[row][col] = AI_PIECE;
                } else {
                    int[] move = getPlayerMove(input, board, AI_PIECE);
                    int row = move[0];
                    int col = move[1];
                    board[row][col] = AI_PIECE;
                }
                playerTurn = true;
            }

            printBoard(board);

            if (isGameOver(board)) {
                if (playerTurn) {
                    System.out.println("AI wins!");
                } else {
                    System.out.println("Player wins!");
                }
                break;
            }
        }
        input.close();
    }

    private static void initBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "_";
            }
        }
    }

    private static void placePieces(String[][] board, int boardSize) {
        /*for (int i = 0; i < boardSize / 2; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i % 2 == 0) {
                    board[i][j] = PLAYER_PIECE;
                    board[boardSize - 1 - i][j] = AI_PIECE;
                } else {
                    board[i][j] = AI_PIECE;
                    board[boardSize - 1 - i][j] = PLAYER_PIECE;
                }
            }
        }*/
        if(boardSize == 5) {
            // Place player's pieces
            for (int i = 0; i < boardSize / 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if(i == 1 && j == 1) {
                        continue;
                    }
                    board[i][j] = PLAYER_PIECE;
                }
            }

            // Place AI's pieces
            for (int i = boardSize / 2 + 1; i < boardSize; i++) {
                for (int j = 3; j < boardSize; j++) {
                    if(i == 3 && j == 3) {
                        continue;
                    }
                    board[i][j] = AI_PIECE;
                }
            }
        } else if(boardSize == 6) {
            // Place player's pieces
            for (int i = 0; i < boardSize / 2 - 1; i++) {
                for (int j = 0; j < 2; j++) {
                    board[i][j] = PLAYER_PIECE;
                }
            }

            // Place AI's pieces
            for (int i = boardSize / 2 + 1; i < boardSize; i++) {
                for (int j = 4; j < boardSize; j++) {
                    board[i][j] = AI_PIECE;
                }
            }
        } else if(boardSize == 8) {
            // Place player's pieces
            for (int i = 0; i < boardSize / 2 - 2; i++) {
                for (int j = 0; j < 2; j++) {
                    board[i][j] = PLAYER_PIECE;
                }
            }

            // Place AI's pieces
            for (int i = boardSize / 2 + 2; i < boardSize; i++) {
                for (int j = 6; j < boardSize; j++) {
                    board[i][j] = AI_PIECE;
                }
            }
        }
    }

    private static void printBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[] getPlayerMove(Scanner input, String[][] board, String playerPiece) {
        while (true) {
            System.out.println("Enter your move (row col):");
            int row = input.nextInt();
            int col = input.nextInt();

            if (isValidMove(board, row, col, playerPiece)) {
                return new int[] {row, col};
            } else {
                System.out.println("Invalid move, try again.");
            }
        }
    }

    /*private static int[] getAIMove(String[][] board, String aiPiece) {
        Random random = new Random();
        while (true) {
            int row = random.nextInt(board.length);
            int col = random.nextInt(board[0].length);
            // check if the random move is valid
            if (isValidMove(board, row, col, aiPiece)) {
                return new int[] {row, col};
            }
        }
    }*/

    private static int[] getAIMove(String[][] board, String aiPiece) {
        int[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(" ")) {
                    board[i][j] = aiPiece;
                    int score = simulateGame(board, aiPiece);

                    board[i][j] = " ";

                    if (score > bestScore) {
                        bestMove = new int[] {i, j};
                        bestScore = score;
                    }
                }
            }
        }
        return bestMove;
    }

    private static int simulateGame(String[][] simulatedBoard, String aiPiece) {
        Random random = new Random();
        while (true) {
            int x1 = random.nextInt(boardSize);
            int y1 = random.nextInt(boardSize);
            int x2 = random.nextInt(boardSize);
            int y2 = random.nextInt(boardSize);
            if (isValidMove(simulatedBoard, x2, y2, AI_PIECE)) {
                simulatedBoard[x2][y2] = simulatedBoard[x1][y1];
                simulatedBoard[x1][y1] = " ";
                if (hasWon(simulatedBoard, aiPiece)) {
                    return 1;
                }
            }

            String[] playerPieces = {"X", "O"};
            for (String piece : playerPieces) {
                if (!piece.equals(aiPiece)) {
                    x1 = random.nextInt(boardSize);
                    y1 = random.nextInt(boardSize);
                    x2 = random.nextInt(boardSize);
                    y2 = random.nextInt(boardSize);
                    if (isValidMove(simulatedBoard, x2, y2, piece)) {
                        simulatedBoard[x2][y2] = simulatedBoard[x1][y1];
                        simulatedBoard[x1][y1] = " ";
                        if (hasWon(simulatedBoard, piece)) {
                            return -1;
                        }
                    }
                }
            }
        }
    }

    private static boolean hasWon(String[][] board, String piece) {
        for (int j = 0; j < boardSize; j++) {
            if (board[boardSize - 1][j].equals(piece)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidMove(String[][] board, int row, int col, String playerPiece) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }
        if (!board[row][col].equals(" ")) {
            return false;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (row + i >= 0 && row + i < board.length && col + j >= 0 && col + j < board[0].length) {
                    if (board[row + i][col + j].equals(playerPiece)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isGameOver(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(PLAYER_PIECE)) {
                    return false;
                }
            }
        }
        return true;
    }
}
