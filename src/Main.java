import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Scanner input = new Scanner(System.in);

        Main.menu();

        String choice = input.next();

        while (!choice.equals("3")){
            switch (choice) {
                case "1":
                    system.register();
                    break;
                case "2":
                    User currentUser = null;
                    while (currentUser == null){
                        System.out.println("Enter Username and Password Respectively: ");
                        String username = input.next();
                        String password = input.next();
                        currentUser = system.login(username, password);
                        if(currentUser != null){
                            System.out.println("Logged in!");
                            currentUser.displayMenu();
                        }else
                            System.out.println("Invalid login details, Please try again");
                    }
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
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