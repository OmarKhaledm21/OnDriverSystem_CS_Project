import java.util.*;

public class OnDriverSystem implements IDataBase{
    private static OnDriverSystem onDriverSystem;

    private Hashtable<String,User> userList;
    private Hashtable<String,User> inActiveUsers;
    private ArrayList<Area> areaList;
    private User currentUser;
    private static DB_Helper db;

    private OnDriverSystem(){
        userList = new Hashtable<String,User>();
        inActiveUsers = new Hashtable<String,User>();
        areaList = new ArrayList<>();
        currentUser = null;
        db = new DB_Helper();
        this.userList.put("admin",new Admin("admin","admin","admin","admin"));
        populateLists();
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

    public void populateLists(){
        ArrayList<User> db_instance = this.db.selectAll();
        for (User user: db_instance) {
            if(user.getStatus()==0){
                this.inActiveUsers.put(user.getUsername(),user);
            }else{
                this.userList.put(user.getUsername(),user);
            }
        }
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
                this.currentUser = user;
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
        int choice=0;
        while (true) {
            try {
                choice = user_input.nextInt();

                break;
            } catch (Exception e) {
                System.out.println("Invalid choice");
                user_input.nextLine();
            }
            System.out.println("Do you want to register as 1- Customer , 2- Driver");
        }

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

        // TODO TEST OMAR ATEF
        if(choice == 2) {
            System.out.println("Enter National ID, License Number: ");
            String nationalID = user_input.next();
            String licenseNumber = user_input.next();
            user = new Captain(userName,password,email,mobileNumber,nationalID,licenseNumber,null,0);
            this.inActiveUsers.put(userName,  user);
            this.db.addUser(user);
        }else{
            user = new Customer(userName,password,email,mobileNumber,1);
            this.userList.put(userName,user);
            this.db.addUser(user);
        }

        System.out.println("User registered successfully");
    }


    public void newRideNotify(Ride ride){
        for (String user_name : userList.keySet()){
            User current = userList.get(user_name);
            if(current instanceof Captain){
                Captain captain = (Captain) current;
                if(captain.getRide()==null){
                    if(ride.getSource().isFavouriteDriver(captain)){
                        captain.notify(new FavAreaRideNotification(ride));
                    }else{
                        captain.notify(new NewRideNotification(ride));
                    }
                }
            }else{
                continue;
            }
        }
    }

    @Override
    public void addArea(Area area) {
        db.addArea(area);
    }

    @Override
    public boolean addUser(User user) {
        return db.addUser(user);
    }

    @Override
    public boolean activateUser(User user) {
        return db.activateUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return db.deleteUser(user);
    }

    @Override
    public User search(String username) {
        return db.search(username);
    }

    @Override
    public boolean userExist(User user) {
        return db.userExist(user);
    }

    @Override
    public boolean suspendUser(User user) {
        return db.suspendUser(user);
    }

    @Override
    public ArrayList<RideEvent> getEvents(Ride ride) {
        return db.getEvents(ride);
    }

    public void saveEvent(RideEvent log) {
        db.saveEvent(log);
    }

    @Override
    public void addRide(Ride ride) {
        db.addRide(ride);
    }

    @Override
    public void changeRideStatus(Ride ride) {
        db.changeRideStatus(ride);
    }

    @Override
    public Ride searchRide(int rideID) {
        return db.searchRide(rideID);
    }

    @Override
    public Area searchArea(String location) {
        return db.searchArea(location);
    }


    public void updateCaptain(Captain captain) {
        db.updateCaptain(captain);
    }

    @Override
    public void addNotification(Notification notification, Captain captain) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void driverMoved(Captain captain) {
        db.driverMoved(captain);
    }

    @Override
    public void addOffer(Offer offer) {
        // TODO Auto-generated method stub
        
    }
}



