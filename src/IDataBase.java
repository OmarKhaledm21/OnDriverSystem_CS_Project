import java.util.ArrayList;

public interface IDataBase {
    public boolean addUser(User user);
    public boolean activateUser(User user);
    public boolean deleteUser(User user);
    public boolean suspendUser(User user);
    public User search(String username);
    public boolean userExist(User user);
    public ArrayList<RideEvent> getEvents(Ride ride);
    public void saveEvent(RideEvent log);
    public void addRide(Ride ride);
    public void changeRideStatus(Ride ride);
    public void addArea(Area area);
    public Ride searchRide(int rideID);
    public Area searchArea(String location);
    public void driverMoved(Captain captain);
    public void updateCaptain(Captain captain);
    public void addNotification(Notification notification, Captain captain);
    public void addOffer(Offer offer);
    public ArrayList<String> searchLogs(int RideID);
    public void deleteNotification(int notifId);
    public ArrayList<Offer> getOffers(int rideId);
}

