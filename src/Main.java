import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Scanner input = new Scanner(System.in);
        Main.menu();
        String choice = input.next();

        while (!choice.equals("3")){

            switch (choice) {
                case "1" -> system.register();
                case "2" -> system.login();
                default -> System.out.println("Invalid command!");
            }


            Main.menu();
            choice = input.next();

        }

        input.close();
    }

    public static void menu(){
        System.out.println("Welcome to OnDriver");
        System.out.println("1- Register\n2- Login\n3- Exit");
    }
}