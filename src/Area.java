import java.util.ArrayList;

public class Area {
    private String location;
    private ArrayList<Captain> pinnedCaptain;

    public Area(String location){

        this.location = location;
        this.pinnedCaptain = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void addToPinnedDrivers(Captain captain){
        this.pinnedCaptain.add(captain);
    }

    public boolean isFavouriteDriver(Captain captain){
        return pinnedCaptain.contains(captain);
    }
}
