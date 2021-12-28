package com.ondriver.api;
import com.ondriver.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("api/v1/ondriver")
@RestController
public class ApiController implements IDataBase {
    private DB_Helper db = new DB_Helper();

    @GetMapping
    public String testConnection(){
        return "It Works :D";
    }

    @GetMapping(path = "/db")
    public ArrayList<User> selectAll(){
        return db.selectAll();
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
    public void driverMoved(Captain captain) {
        db.driverMoved(captain);
    }

    @Override
    public ArrayList<String> searchLogs(int RideID) {
        return db.searchLogs(RideID);
    }

    @Override
    public ArrayList<Area> getAreas() {
        return db.getAreas();
    }

    @Override
    public boolean isNewUser(String username) {
        return db.isNewUser(username);
    }

    @Override
    public void updateCustomerRides(String username) {
        db.updateCustomerRides(username);
    }

    @Override
    public double checkHoliday(String date) {
        return db.checkHoliday(date);
    }

    @Override
    public ArrayList<Ride> getRidesHistory(User user) {
        return db.getRidesHistory(user);
    }

    @Override
    public int rideCounter() {
        Ride.setRide_id(db.rideCounter());
        return 0;
    }
}
