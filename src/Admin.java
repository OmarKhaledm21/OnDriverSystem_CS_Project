import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class Admin extends User{


    public Admin(String username,String password,String email,String mobileNumber){
        super(username,password,email, mobileNumber);
    }

    public void verifyRegistration(){
        OnDriverSystem system =OnDriverSystem.getSystem();
        Hashtable<String,User> inActive = system.getInActiveUsers();

        int choice=0;
        Scanner in = new Scanner(System.in);
        for (String key :inActive.keySet()){
            System.out.println("What you want to do?\n1- to Verify 2- to Delete (anything else to skip)");
            switch (choice){
                case 1:
                    system.getUserList().put(key,inActive.get(key));
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
   public void listRegistrations(){
        OnDriverSystem system =OnDriverSystem.getSystem();
        Hashtable<String,User> inActive = system.getInActiveUsers();

        int count=0;
        for (String key :inActive.keySet()){
            count++;
            if (inActive.get(key) instanceof Customer){
                System.out.println(count+ " Customer : " + key );
            }else if (inActive.get(key) instanceof Driver){
                System.out.println( count + ": Driver : " + key );
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
           System.out.println("Enter UserName of the User/Driver you want to suspend");
           Scanner in = new Scanner(System.in);
           username = in.next();
           if (userHashtable.containsKey(username)) {
               inActive.put(username, userHashtable.get(username));
               userHashtable.remove(username);
               loop=false;
           } else {
               System.out.println("User not found please re-try");
           }


       }
   }
}
