package com.ondriver;
import com.ondriver.api.ApiController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OnDriverSystem system = OnDriverSystem.getSystem();
        system.setDB(new ApiController());
        Scanner input = new Scanner(System.in);

        Main.menu();

        String choice = input.next();

        while (!choice.equals("3")){
            switch (choice) {
                case "1":
                    system.register();
                    break;
                case "2":
                    User currentUser = system.login();
                    if(currentUser!=null){
                        currentUser.displayMenu();
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