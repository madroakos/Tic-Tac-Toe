import java.util.Random;
import java.util.Scanner;

public class Map {


    private final int xLength = 3;
    private final int yLength = 3;
    private String[][] board = new String[xLength][yLength];
    private gameMode currentPlayer;
    private gameMode player1, player2;
    private String currentSymbol = "X";
    private int remainingMoves = (xLength * yLength) - 1;

    private boolean isOver = false;

    private enum State{Blank, X, O}
    private enum gameMode{USER, EASY, MEDIUM}

    //Constructor, Getters and Setters ---------------------------------------

    public Map(String player1Helper, String player2Helper) {
        //fill map with empty spaces
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                board[i][j] = "_";
            }
        }

        //Set players
        player1 = gameMode.valueOf(player1Helper.toUpperCase());
        player2 = gameMode.valueOf(player2Helper.toUpperCase());

        //Set starting player
        currentPlayer = player1;
    }

    //Methods-----------------------------------------------------------------

    private boolean isEmpty (int x, int y) {
        return board[x][y].equals("_");
    }

    public boolean isOver () {
        return isOver;
    }

    private boolean inRange (int x, int y) {
        if (x < 0 || x > xLength - 1) {
            return false;
        } else return y >= 0 && y <= yLength - 1;
    }

    private void changeCurrentPlayer() {
        if (remainingMoves % 2 == 1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    public void showBoard () {
        System.out.println("---------");
        for (int i = 0; i < xLength; i++) {
            System.out.printf("| ");
            for (int j = 0; j < yLength; j++) {
                if (board[i][j].equals("_")) {
                    System.out.printf("  ");
                } else {
                    System.out.printf(board[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public void nextTurn () {
        if (currentPlayer.equals(gameMode.USER)) {
            String move;
            do {
                move = requestCoordinate();
            } while (move.equals("error"));
            registerMove(Integer.parseInt(move.split(" ")[0]), Integer.parseInt(move.split(" ")[1]));
        } else if (currentPlayer.equals(gameMode.EASY)) {
            System.out.println("Making move level \"easy\"");
            Random random = new Random();
            int x, y;
            do {
                x = random.nextInt(3 - 1 + 1);
                y = random.nextInt(3 - 1 + 1);
            } while (!inRange(x, y) || !isEmpty(x, y));
            registerMove(x, y);
        } else if (currentPlayer.equals(gameMode.MEDIUM)) {
            System.out.println("Making move level \"medium\"");
            String helper = canWin(State.valueOf(currentSymbol));
            if (helper.equals("")) {
                changeSymbol();
                helper = canWin(State.valueOf(currentSymbol));
                changeSymbol();
                if (helper.equals("")) {
                    Random random = new Random();
                    int x, y;
                    do {
                        x = random.nextInt(3 - 1 + 1);
                        y = random.nextInt(3 - 1 + 1);
                    } while (!inRange(x, y) || !isEmpty(x, y));
                    registerMove(x, y);
                } else {
                    registerMove(Integer.parseInt(helper.split(" ")[0]), Integer.parseInt(helper.split(" ")[1]));
                }
            } else {
                registerMove(Integer.parseInt(helper.split(" ")[0]), Integer.parseInt(helper.split(" ")[1]));
            }
        }
        changeCurrentPlayer();
    }

    private String requestCoordinate () {
        int tempX = 0, tempY = 0;
        System.out.print("Enter the coordinates: ");
        try {
            Scanner scan = new Scanner(System.in);
            tempX = scan.nextInt();
            tempY = scan.nextInt();

            if (!inRange(tempX - 1, tempY - 1)) {
                System.out.println("Coordinates should be from 1 to 3!");
                return "error";
            } else if (!isEmpty(tempX - 1, tempY - 1)) {
                System.out.println("This cell is occupied! Choose another one!");
                return "error";
            }
        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            return "error";
        }
        return (tempX - 1) + " " + (tempY - 1);
    }

    private void registerMove(int x, int y) {
        board[x][y] = currentSymbol;
        showBoard();
        System.out.print(gameState(x, y, State.valueOf(currentSymbol)));
        changeSymbol();
        remainingMoves--;
    }

    private void changeSymbol () {
        if (currentSymbol.equals("X")) {
            currentSymbol = "O";
        } else {
            currentSymbol = "X";
        }
    }

    private String canWin (State s) {
        String helperString = "";
        int counter = 0;
        //check col
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (!board[i][j].equals(s.name())) {
                    if (board[i][j].equals("_")) {
                        helperString = i + " " + j;
                    }
                } else {
                    counter++;
                }
            }
            if (counter == yLength - 1 && !helperString.equals("")) {
                return helperString;
            } else {
                helperString = "";
                counter = 0;
            }
        }



        //check row
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (!board[j][i].equals(s.toString())) {
                    if (board[j][i].equals("_")) {
                        helperString = j + " " + i;
                    }
                } else {
                    counter++;
                }
            }
            if (counter == yLength - 1 && !helperString.equals("")) {
                return helperString;
            } else {
                helperString = "";
                counter = 0;
            }
        }


        //check diag
        for (int i = 0; i < xLength; i++) {
            if (!board[i][i].equals(s.toString())) {
                if (board[i][i].equals("_")) {
                    helperString = i + " " + i;
                }
            } else {
                counter++;
            }
        }
        if (counter == xLength - 1 && !helperString.equals("")) {
            return helperString;
        } else {
            helperString = "";
            counter = 0;
        }

        //check anti-diag
        for (int i = 0; i < xLength; i++) {
            if (!board[i][(xLength - 1) - i].equals(s.toString())) {
                if (board[i][(xLength - 1) - i].equals("_")) {
                    helperString = i + " " + ((xLength - 1) - i);
                }
            } else {
                counter++;
            }
        }
        if (counter == xLength - 1 && !helperString.equals("")) {
            return helperString;
        } else {
            helperString = "";
            counter = 0;
        }
        return "";
    }

    private String gameState (int x, int y, State s) {
        //check col
        for (int i = 0; i < yLength; i++) {
            if (!board[x][i].equals(s.toString())) {
                break;
            } else if (i == yLength - 1) {
                isOver = true;
                return (s + " wins\n");
            }
        }

        //check row
        for (int i = 0; i < xLength; i++) {
            if (!board[i][y].equals(s.toString())) {
                break;
            } else if (i == xLength - 1) {
                isOver = true;
                return (s + " wins\n");
            }
        }

        //check diag
        if (x == y) {
            for (int i = 0; i < xLength; i++) {
                if (!board[i][i].equals(s.toString())) {
                    break;
                } else if (i == xLength - 1) {
                    isOver = true;
                    return (s + " wins\n");
                }
            }
        }

        //check anti-diag
        if (x + y == xLength - 1) {
            for (int i = 0; i < xLength; i++) {
                if (!board[i][(xLength - 1) - i].equals(s.toString())) {
                    break;
                } else if (i == xLength - 1) {
                    isOver = true;
                    return (s + " wins\n");
                }
            }
        }

        //check draw
        if (remainingMoves == 0) {
            isOver = true;
            return "Draw";
        }
        return "";
    }
}
