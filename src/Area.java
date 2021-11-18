import java.util.ArrayList;

public class Area {
    private String location;
    private ArrayList<Driver> pinnedDriver;

    public Area(String location){

        this.location = location;
        this.pinnedDriver = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void addToPinnedDrivers(Driver driver){
        this.pinnedDriver.add(driver);
    }

    public boolean isFavouriteDriver(Driver driver){
        return pinnedDriver.contains(driver);
    }
}
