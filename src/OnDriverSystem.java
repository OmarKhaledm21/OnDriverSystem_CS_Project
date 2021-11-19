import java.util.*;

public class OnDriverSystem {
    private static OnDriverSystem onDriverSystem;

    private Hashtable<String,User> userList;
    private Hashtable<String,User> inActiveUsers;
    private ArrayList<Area> areaList;
    private User currentUser;

    private OnDriverSystem(){
        userList = new Hashtable<String,User>();
        inActiveUsers = new Hashtable<String,User>();
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

    public Hashtable<String,User>getInActiveUsers(){
        return inActiveUsers;
    }

    public Hashtable<String,User>getUserList(){
        return userList;
    }

    public ArrayList<Area> getAreaList() {
        return areaList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setAreaList(ArrayList<Area> areaList) {
        this.areaList = areaList;
    }


    public User login(){
        this.currentUser = null;
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

        while (this.inActiveUsers.containsKey(userName) || this.userList.containsKey(userName)){
            System.out.println("Username is already in use, Please type a new username");
            userName = user_input.next();
        }

        if(choice == 2) {
            System.out.println("Enter National ID, License Number: ");
            String nationalID = user_input.next();
            String licenseNumber = user_input.next();
            user = new Driver(userName,password,email,mobileNumber,nationalID,licenseNumber);
            this.inActiveUsers.put(userName, (Driver) user);
        }else{
            user = new Customer(userName,password,email,mobileNumber);
            this.userList.put(userName,user);
        }


        System.out.println("User registered successfully");
    }

    public void newRideNotify(Ride ride){
        for (String user_name : userList.keySet()){
            User current = userList.get(user_name);
            if(current instanceof Driver){
                Driver driver = (Driver) current;
                if(driver.getRide()==null){
                    if(ride.getSource().isFavouriteDriver(driver)){
                        driver.notify(new FavAreaRideNotification(ride));
                    }else{
                        driver.notify(new NewRideNotification(ride));
                    }
                }
            }
        }
    }

    public void addArea(Area area){
        areaList.add(area);
    }
}