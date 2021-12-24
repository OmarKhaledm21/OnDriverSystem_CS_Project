public interface IDataBase {
    public boolean addUser(User user);
    public boolean activateUser(User user);
    public boolean deleteUser(User user);
    public boolean suspendUser(User user);
    public User search(String username);
    public boolean userExist(User user);
    public RideEvent getEvent(Ride ride);
    public void saveEvent(RideEvent log);
    public void addRide(Ride ride);
    public void changeRideStatus(Ride ride);
    public void addArea(Area area);
    public Ride searchRide(Ride ride);
    public Area searchArea(String location);
}

