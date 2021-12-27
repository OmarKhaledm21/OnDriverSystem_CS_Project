import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Admin extends User {
    public Admin(String username, String password, String email, String mobileNumber) {
        super(username, password, email, mobileNumber, 1);
    }

    public void setDiscount(String location){
        ArrayList<Area> areas = OnDriverSystem.getSystem().getAreaList();
        if(areas!=null){
            for (Area area: areas) {
                if(area.getLocation().equals(location)){
                    area.setDiscount(0.1);
                }
            }
        }else{
            System.out.println("Area not found in system!");
        }
    }

    public void verifyRegistration() {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> inActive = system.getInActiveUsers();
        Hashtable<String, User> systemUserList = system.getUserList();

        Hashtable<String, User> clonedUsers = (Hashtable<String, User>) inActive.clone();

        Scanner in = new Scanner(System.in);
        User user = null;
        int choice = 0;

        for (String key : clonedUsers.keySet()) {
            if (clonedUsers.get(key) instanceof Captain) {
                System.out.println("Driver : " + key);
                System.out.println(system.search(key).toString());
            } else if (clonedUsers.get(key) instanceof Customer) {
                System.out.println("Customer : " + key);
                System.out.println(system.search(key).toString());
            }

            //TODO//TODO//TODO//TODO//TODO//TODO//TODO CHANGE STATUS FOR DB

            user = inActive.get(key); //TODO

            System.out.println("What you want to do?\n1- to Verify 2- to Delete (anything else to skip)");
            choice = in.nextInt();
            switch (choice) {
                case 1:
                    system.activateUser(user);
                    systemUserList.put(key, inActive.get(key));
                    inActive.remove(key);
                    break;
                case 2:
                    system.deleteUser(user);
                    inActive.remove(key);
                    break;
                default:
                    System.out.println("Skipping");
                    break;
            }
        }
    }

    public void listInActiveUsers() {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> inActive = system.getInActiveUsers();

        int count = 0;
        if (inActive.isEmpty()) {
            System.out.println("No inActive Users at the moment");
        } else {
            for (String key : inActive.keySet()) {
                count++;
                if (inActive.get(key) instanceof Customer) {
                    System.out.println(count + ": Customer : " + key);
                } else if (inActive.get(key) instanceof Captain) {
                    System.out.println(count + ": Driver : " + key);
                }
            }
        }
    }


    public void listActiveUsers() {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> users = system.getUserList();

        int count = 0;
        if (users.isEmpty()) {
            System.out.println("User list is Empty");
        } else {
            for (String key : users.keySet()) {
                count++;
                if (users.get(key) instanceof Customer) {
                    System.out.println(count + ": Customer : " + key);
                } else if (users.get(key) instanceof Captain) {
                    System.out.println(count + ": Driver : " + key);
                } else if (users.get(key) instanceof Admin) {
                    System.out.println(count + ": Admin : " + key);
                }
            }
        }
    }

    public void listRideLogs(int rideID) {
        Ride ride = OnDriverSystem.getSystem().searchRide(rideID);
        if (ride != null) {
            OnDriverSystem.getSystem().getEvents(ride);
        }
    }

    //TODO
    public void suspendUser() {
        OnDriverSystem system = OnDriverSystem.getSystem();
        Hashtable<String, User> userHashtable = system.getUserList();
        Hashtable<String, User> inActive = system.getInActiveUsers();
        Scanner in = new Scanner(System.in);
        boolean loop = true;
        String username = "";

        while (loop) {
            listActiveUsers();
            System.out.println("Enter UserName of the User/Driver you want to suspend");
            username = in.next();

            User user = system.search(username);
            system.suspendUser(user);

            if (userHashtable.containsKey(username)) {
                inActive.put(username, userHashtable.get(username));
                userHashtable.remove(username);
                loop = false;
            } else if (!username.equals("exit"))
                System.out.println("User not found please re-try");
            else {
                loop = false;
            }
        }
    }

    public void getLogs(int RideID) {
        ArrayList<String> logs = OnDriverSystem.getSystem().searchLogs(RideID);
        if (logs.size() == 0) {
            System.out.println("No Logs Are Available for Ride with ID " + RideID);
        } else {
            for (int i = 0 ; i < logs.size();i++){
                System.out.println(logs.get(i));
            }
        }
    }


    @Override
    public void displayMenu() {
        Admin admin = this;
        int choice = 10;
        Scanner in = new Scanner(System.in);
        while (choice != 0) {
            System.out.println("Welcome " + this.getUsername() + " there is your menu \n" +
                    "1- List Active Users\n2- List inActive Users\n3- Suspend User\n4- Verify Registration\n5. View Logs\n6. Set discount\n0- Exit");
            choice = in.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    listActiveUsers();
                    break;
                case 2:
                    listInActiveUsers();
                    break;
                case 3:
                    suspendUser();
                    break;
                case 4:
                    verifyRegistration();
                    break;
                case 5:
                    System.out.println("Enter Ride ID u want to View Logs for [From 1 to "+Ride.ride_id+"]");
                    int RideID = in.nextInt();
                    getLogs(RideID);
                    break;
                case 6:
                    String location = in.next();
                    setDiscount(location);
                    break;
                default:
                    System.out.println("Invalid Choice");
                    break;
            }
        }

    }
}
