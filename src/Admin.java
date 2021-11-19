import java.util.Hashtable;
import java.util.Scanner;

public class Admin extends User{
    public Admin(String username,String password,String email,String mobileNumber){
        super(username,password,email, mobileNumber);
    }

    public void verifyRegistration(){
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String,User> inActive = system.getInActiveUsers();
        Hashtable<String,User> systemUserList = system.getUserList();
        Hashtable<String,User> clonedUsers = (Hashtable<String, User>) inActive.clone();
        int choice=0;
        Scanner in = new Scanner(System.in);
        for (String key: clonedUsers.keySet()){
            if (clonedUsers.get(key) instanceof Driver) {
                System.out.println("Driver : " + key);
            } else if (clonedUsers.get(key) instanceof Customer) {
                System.out.println("Customer : " + key);
            }
            System.out.println("What you want to do?\n1- to Verify 2- to Delete (anything else to skip)");
            choice = in.nextInt();
            switch (choice){
                case 1:
                    systemUserList.put(key,inActive.get(key));
                    inActive.remove(key);
                    break;
                case 2:
                    inActive.remove(key);
                    break;
                default :
                    System.out.println("Skipping");
                    break;
            }
        }
    }

    public void listInActiveUsers(){
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> inActive = system.getInActiveUsers();

        int count=0;
        if (inActive.isEmpty()){
            System.out.println("No inActive Users at the moment");
        }
        else{
            for (String key: inActive.keySet()){
                count++;
                if (inActive.get(key) instanceof Customer){
                    System.out.println(count+ ": Customer : " + key );
                }else if (inActive.get(key) instanceof Driver){
                    System.out.println( count + ": Driver : " + key );
                }
            }
        }
    }


    public void listActiveUsers(){
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> users = system.getUserList();

        int count = 0;
        if (users.isEmpty()){
            System.out.println("User list is Empty");
        }
        else{
            for (String key: users.keySet()){
                count++;
                if (users.get(key) instanceof Customer){
                    System.out.println(count+ ": Customer : " + key );
                } else if (users.get(key) instanceof Driver){
                    System.out.println( count + ": Driver : " + key );
                } else if (users.get(key) instanceof Admin){
                    System.out.println( count + ": Admin : " + key );
                }
            }
        }
    }

    public void suspendUser() {
        OnDriverSystem system = OnDriverSystem.getSystem();

        Hashtable<String, User> userHashtable = system.getUserList();
        Hashtable<String, User> inActive = system.getInActiveUsers();
        boolean loop = true;

        while (loop) {
            String username = "";
            listActiveUsers();
            System.out.println("Enter UserName of the User/Driver you want to suspend");
            Scanner in = new Scanner(System.in);
            username = in.next();

            if (userHashtable.containsKey(username)) {
                inActive.put(username, userHashtable.get(username));
                userHashtable.remove(username);
                loop = false;
            } else if(!username.equals("exit")){
                System.out.println("User not found please re-try");
            } else{ loop = false; }
        }
    }

    @Override
    public void displayMenu() {
        Admin admin = this;
        int choice=69;
        Scanner in = new Scanner(System.in);
        while (choice!=0){
            System.out.println("Welcome "+this.getUsername()+" there is your menu \n" +
                    "1- List Active Users\n2- List inActive Users\n3- Suspend User\n4- Verify Registration\n0- Exit");
            choice=in.nextInt();
            switch (choice){
                case 0 : break;
                case 1:
                    listActiveUsers();
                    break;
                case 2:
                    listInActiveUsers();
                    break;
                case 3 :
                    suspendUser();
                    break;
                case 4 :
                   verifyRegistration();
                   break;

                default:
                    System.out.println("Invalid Choice");
                    break;
             }
        }

    }
}
