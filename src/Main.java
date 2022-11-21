import java.util.Scanner;

public class Main {

    private enum gameMode{USER, EASY, MEDIUM}
    private enum gameCtrl{START, EXIT}

    public static void main(String[] args) {
        String input = gameModeInput();
        if (!input.equals("exit")) {
            Map currentMap = new Map(input.split(" ")[1], input.split(" ")[2]);
            currentMap.showBoard();
            do {
                currentMap.nextTurn();
            } while (!currentMap.isOver());
        }
    }


    private static boolean inGameMode (String x) {
        for (gameMode g : gameMode.values()) {
            if (g.name().contains(x)) {
                return true;
            }
        }
        return false;
    }

    private static boolean inGameCtrl (String x) {
        for (gameCtrl g : gameCtrl.values()) {
            if (g.name().contains(x)) {
                return true;
            }
        }
        return false;
    }

    private static boolean inputCheck (String x) {
        String msg = "Bad parameters!\nInput command: ";
        switch (x.split(" ").length) {
            case 1:
                if (x.equals("exit")) {
                    return true;
                } else if (x.equals("start")) {
                    System.out.print(msg);
                    return false;
                }
            case 2:
                System.out.print(msg);
                return false;
            case 3:
                if (inGameCtrl(x.split(" ")[0].toUpperCase()) && inGameMode(x.split(" ")[1].toUpperCase()) && inGameMode(x.split(" ")[2].toUpperCase())) {
                    return true;
                } else {
                    System.out.print(msg);
                    return false;
                }
            default :
                System.out.print(msg);
                return false;
        }
    }

    public static String gameModeInput () {
        String input = "error";
        System.out.print("Input command: ");
        Scanner scan = new Scanner (System.in);
        do {
            try {
                input = scan.nextLine();
            } catch (Exception e) {
                System.out.println("Bad parameters!");
            }
        } while (!inputCheck(input));
        if (input.split(" ")[0].equals("exit")) {
            return "exit";
        }
        return input.toUpperCase();
    }
}
