package com.ondriver;
import java.util.ArrayList;

public interface IDataBase {
    public ArrayList<Ride> getRidesHistory(User user);
    public ArrayList<User> selectAll();
    public int rideCounter();
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
    public ArrayList<String> searchLogs(int RideID);
    public ArrayList<Area> getAreas();
    public void updateCustomerRides(String username);
    public boolean isNewUser(String username);
    public boolean addHoliday(String date);
    public double checkHoliday(String date);
    public double getRating(String username);
    public double getBalance(String username);
}

