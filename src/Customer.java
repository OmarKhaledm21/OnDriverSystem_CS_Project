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
}
