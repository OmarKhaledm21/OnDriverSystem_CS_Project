import java.util.ArrayList;
import java.util.Scanner;

public class Customer extends User {
    private Ride ride;

    public Customer(String username,String password,String email,String mobileNumber){
        super(username,password,email,mobileNumber);
        ride = null;
    }

    public void requestRide(){
        OnDriverSystem system = OnDriverSystem.getSystem();
        ArrayList<Area> areas = system.getAreaList();
        Scanner input = new Scanner(System.in);

        System.out.println("Enter source and destination areas: ");
        String src=input.next(),dest=input.next();
        Area source = null,destination = new Area(dest);

        if(areas!=null){
            for(Area area : areas){
                if(area.getLocation().equals(src)){
                    source = area;
                }
            }
        }else{
            source = new Area(src);
        }

        this.ride = new Ride(source,destination);

        system.newRideNotify(this.ride);
    }

    public void rateRide(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your rating 1-5: ");
        int rating = input.nextInt();
        this.ride.setRating(rating);
        System.out.println("You rate this ride "+rating+" out of 5!");
    }

    public void checkAverageRating(){
        System.out.println("Average rating: "+this.ride.getDriver().getAverageRating());
    }

    public void checkOffers(){
        System.out.println("Checking for available offers....");
        ArrayList<Offer> offers = this.ride.getPriceOffers();
        if(offers != null){
            for(int i = 0; i<offers.size(); i++){
                System.out.println(i+". "+offers.get(i).toString());
                System.out.println("Average Rating: "+offers.get(i).getDriver().getAverageRating());
            }




        }else{
            System.out.println("No offers for this ride yet!");
        }
    }
}
