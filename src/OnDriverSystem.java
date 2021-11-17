import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class OnDriverSystem {
    private static OnDriverSystem onDriverSystem;

    private Hashtable<String,User> userList;
    private Hashtable<String,Driver> unActiveUsers;
    private ArrayList<Area> areaList;
    private User currentUser;


    private OnDriverSystem(){
        userList = new Hashtable<String,User>();
        unActiveUsers = new Hashtable<String,Driver>();
        areaList = new ArrayList<>();
        currentUser = null;

        this.userList.put("admin",new Admin("admin","admin","admin","admin"));
    }

    public static OnDriverSystem getSystem(){
        if(onDriverSystem ==null){
            onDriverSystem = new OnDriverSystem();
        }
        return onDriverSystem;
    }

    public User login(){
        Scanner user_input = new Scanner(System.in);
        System.out.println("Enter Username and Password Respectively: ");
        String userName,password;
        userName = user_input.next();
        password = user_input.next();

        User user = this.userList.get(userName);

        if(user != null){
            if(user.getPassword().equals(password)){
                this.currentUser = user;
                System.out.println("Logged in!");
            }else{
                while (!user.getPassword().equals(password)){
                    System.out.println("Wrong password, please retype your password!");
                    password = user_input.next();
                }
                System.out.println("Logged in!");
            }
        }else{
            System.out.println("User is not registered in the system!");
        }



        return this.currentUser;
    }

    public void register(){
        Scanner user_input = new Scanner(System.in);
        System.out.println("Do you want to register as 1- Customer , 2- Driver");
        int choice = user_input.nextInt();
        User user;
        System.out.println("Enter Username, Mobile Number, Email and Password Respectively: ");
        String userName,mobileNumber,email,password;
        userName = user_input.next();
        mobileNumber = user_input.next();
        email = user_input.next();
        password = user_input.next();

        while (this.unActiveUsers.containsKey(userName) || this.userList.containsKey(userName)){
            System.out.println("Username is already in use, Please type a new username");
            userName = user_input.next();
        }

        if(choice == 2) {
            System.out.println("Enter National ID, License Number: ");
            String nationalID = user_input.next();
            String licenseNumber = user_input.next();
            user = new Driver(userName,password,email,mobileNumber,nationalID,licenseNumber);
            this.unActiveUsers.put(userName, (Driver) user);
        }else{
            user = new Customer(userName,password,email,mobileNumber);
            this.userList.put(userName,user);
        }


        System.out.println("User registered successfully");
    }
}
