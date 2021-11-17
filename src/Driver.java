import java.util.ArrayList;
import java.util.Scanner;

public class Driver extends User{
    private String nationalID;
    private String licenseNumber;
    private double averageRating;
    private ArrayList<Notification> notificationList;
    private Ride ride;
    private ArrayList<Ride> rides;

    public Driver(){
        this.averageRating = 0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.rides = new ArrayList<>();
    }

    public Driver(String username,String password,String email,String mobileNumber,String nationalID,String licenseNumber){
        super(username,password,email,mobileNumber);
        this.nationalID = nationalID;
        this.licenseNumber = licenseNumber;
        this.averageRating =0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.rides = new ArrayList<>();
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
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
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
            Ride newRide = notification.getRide();
            this.ride = newRide;
            this.rides.add(newRide);
        }
        else {
            this.notificationList.add(notification);
        }
    }

    public void manageNotification(){
        for (int i = 0; i < this.notificationList.size(); i++){
            System.out.println(i + ". " + this.notificationList.get(i).getRide().toString());
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
