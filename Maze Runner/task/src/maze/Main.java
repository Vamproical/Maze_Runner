package maze;

import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private boolean isLoaded = false;

    private void menu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if (isLoaded) {
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
        }
        System.out.println("0. Exit");
    }

    public void init() {
        boolean flag = false;
        MazeRunner mazeRunner = null;
        while (!flag) {
            menu();
            String str = scanner.nextLine();
            switch (str) {
                case "1":
                    System.out.println("Please, enter the size of a maze");
                    int row = scanner.nextInt();
                    mazeRunner = new MazeRunner(row, true);
                    System.out.println(mazeRunner.toString());
                    scanner.nextLine();
                    isLoaded = true;
                    break;
                case "2":
                    System.out.println("Enter path to the file:");
                    String path = scanner.nextLine();
                    mazeRunner = MazeRunner.readFromFile(path);
                    isLoaded = true;
                    break;
                case "3":
                    String pathToSave = scanner.nextLine();
                    mazeRunner.writeToFile(pathToSave);
                    break;
                case "4":
                    System.out.println(mazeRunner.toString());
                    break;
                case "0":
                    flag = true;
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
        }
    }


    public static void main(String[] args) {
        Main main = new Main();
        main.init();
    }
}