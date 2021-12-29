package com.ondriver.api;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @PostMapping(path = "/addArea")
    public void addArea(@RequestBody Area area) {
        db.addArea(area);
    }

    @Override
    public boolean addUser(User user) {
        return db.addUser(user);
    }

    @PostMapping(path = "/addCustomer")
    public boolean addCustomer(@RequestBody Customer user) {
        return this.addUser(user);
    }

    @PostMapping(path = "/addCaptain")
    public boolean addCaptain(@RequestBody Captain user) {
        return this.addUser(user);
    }

    @Override
    public boolean activateUser(User user) {
        return db.activateUser(user);
    }

    @PostMapping(path = "/activate/{username}")
    public boolean activateUser(@PathVariable String username){
        User user = db.search(username);
        return this.activateUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return db.deleteUser(user);
    }

    @PostMapping(path = "/delete/{username}")
    public boolean deleteUser(@PathVariable String username){
        User user = db.search(username);
        return this.deleteUser(user);
    }

    //TODO TODO TODO TODO TODO TODO
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
    @GetMapping(path = "/getAreas")
    public ArrayList<Area> getAreas() {
        return db.getAreas();
    }

    @Override
    public boolean isNewUser(String username) {
        return db.isNewUser(username);
    }

    @Override
    @PostMapping(path = "/updateCustomerRides/{username}")
    public void updateCustomerRides(@PathVariable String username) {
        db.updateCustomerRides(username);
    }

    @Override
    public boolean addHoliday(@PathVariable String date) {
        return db.addHoliday(date);
    }

    @Override
    public double checkHoliday(String date) {
        return db.checkHoliday(date);
    }

    @Override
    public ArrayList<Ride> getRidesHistory(User user) {
        return db.getRidesHistory(user);
    }

    @GetMapping(path = "/getRidesHistory/{username}")
    public ArrayList<Ride> getRidesHistory(String username) {
        User user = search(username);
        return this.getRidesHistory(user);
    }

    @Override
    public int rideCounter() {
        Ride.setRide_id(db.rideCounter());
        return 0;
    }
}
