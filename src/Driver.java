import java.util.ArrayList;
import java.util.Scanner;

public class Driver extends User{
    private String nationalID;
    private String licenseNumber;
    private double averageRating;
    private ArrayList<Notification> notificationList;
    private Ride ride;
    private ArrayList<Ride> ridesHistory;

    public Driver(){
        this.averageRating = 0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.ridesHistory = new ArrayList<>();
    }

    public Driver(String username,String password,String email,String mobileNumber,String nationalID,String licenseNumber){
        super(username,password,email,mobileNumber);
        this.nationalID = nationalID;
        this.licenseNumber = licenseNumber;
        this.averageRating =0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.ridesHistory = new ArrayList<>();
    }

    ///////////////////////////////////// Getters and Setters /////////////////////////////////////

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public double getAverageRating() {
        setAverageRating();
        return averageRating;
    }

    public void setRide(Ride ride){
        this.ride = ride;
    }

    public Ride getRide(){
        return this.ride;
    }

    public void setAverageRating() {
        int sumRating = 0;
        for (Ride ride : ridesHistory){
            sumRating += ride.getRating();
        }
        this.averageRating = sumRating / ridesHistory.size();
    }

    public void offerPrice(Ride ride){
        System.out.println("Enter your offer: ");
        Scanner input = new Scanner(System.in);
        double price = input.nextDouble();
        Offer offeredPrice = new Offer(this, price);
        ride.addOffer(offeredPrice);
    }

    public void notify(Notification notification){
        if (notification instanceof CustomerAcceptedRideNotification){
            this.ride = notification.getRide();
        }else if(notification instanceof FinishedRideNotification){
            this.ridesHistory.add(ride);
            this.ride = null;
        }
        else {
            this.notificationList.add(notification);
        }
    }

    public void manageNotification(){
        if(notificationList.isEmpty()){
            System.out.println("No new notifications");
        }
        else{
            for (int i = 0; i < this.notificationList.size(); i++){
                System.out.println(i + ". " + this.notificationList.get(i).toString());
            }
            Scanner input = new Scanner(System.in);
            int choice = -1;
            int notificationIndex;

            while (true){
                System.out.println("Please enter ride number \nEnter -1 to exit");
                notificationIndex = input.nextInt();
                if (notificationIndex == -1)
                    break;
                if (notificationIndex < notificationList.size() - 1){
                    System.out.println("1- Offer Price 2- Reject Ride");
                    choice = input.nextInt();
                    if (choice == 1){
                        offerPrice(notificationList.get(notificationIndex).getRide());
                    }
                    notificationList.remove(notificationIndex);
                }
            }
        }
    }

    public void addFavouriteArea(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter area name: ");
        String location = input.nextLine();
        Area favouriteArea = new Area(location);
        boolean found = false;
        for (Area area : OnDriverSystem.getSystem().getAreaList()){
            if (location.equals(area.getLocation())){
                area.addToPinnedDrivers(this);
                found = true;
            }
        }
        if (!found){
            favouriteArea.addToPinnedDrivers(this);
            OnDriverSystem.getSystem().addArea(favouriteArea);
        }
    }

    public void listRatings(){
        if(ridesHistory.isEmpty()){
            System.out.println("No rides");
        }
        else{
            for (Ride ride : ridesHistory){
                System.out.println(ride.toString() + "\nRating: " + ride.getRating());
            }
        }
    }

    public void RideStatus(){
        System.out.println(this.ride.getRideStatus());
    }

    @Override
    public void displayMenu() {
        Scanner input = new Scanner(System.in);
        int choice = 0;
        while(choice != 4){
            System.out.println("1. View and manage notifications\n2. View rides history\n3. Add a favourite area\n4. logout");
            choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    manageNotification();
                    break;
                case 2:
                    listRatings();
                    break;
                case 3:
                    addFavouriteArea();
                default:
                    System.out.println("Invalid choice");
                    continue;
            }
        }
    }
}
