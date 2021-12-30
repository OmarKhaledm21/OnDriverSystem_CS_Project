package com.ondriver.api;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ondriver.*;
import org.springframework.http.MediaType;
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
    @GetMapping(path = "/search/{username}")
    public User search( @PathVariable String username) {
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

    @PostMapping(path = "/suspend/{username}")
    public boolean userToSuspend(@PathVariable String username){
        User user = db.search(username);
        return this.suspendUser(user);
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
    @GetMapping(path = "/Events/{RideID}")
    public ArrayList<String> searchLogs(@PathVariable int RideID) {
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
    public ArrayList<Ride> getRidesHistory(@PathVariable String username) {
        User user = search(username);
        return this.getRidesHistory(user);
    }

    @Override
    public int rideCounter() {
        Ride.setRide_id(db.rideCounter());
        return Ride.getRide_id();
    }

    @Override
    public double getRating(String username) {
        return db.getRating(username);
    }

    /**
     * Request Ride Service
     * */
    //TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
    //TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO

    static class RequestHandler{
        public String source;
        public String destination;
        public String username;

        public RequestHandler(@RequestBody @JsonProperty("source") String source,
                              @RequestBody @JsonProperty("destination")String destination,
                              @RequestBody @JsonProperty("username") String username){
            this.source= source;
            this.destination = destination;
            this.username = username;
        }
    }

    @PostMapping(path = "/requestRide/{username}")
    public void requestRide(@RequestBody RequestHandler requestHandler){
        Customer customer = (Customer) OnDriverSystem.getSystem().getUserList().get(requestHandler.username);
        Ride ride = new Ride(customer,new Area(requestHandler.source),new Area(requestHandler.destination),3);

        ride.setCustomer(customer);
        customer.setRide(ride);
        int newRideID = OnDriverSystem.getSystem().rideCounter();
        ride.setID(++newRideID);
        OnDriverSystem.getSystem().newRideNotify(ride);
    }

    @GetMapping(path = "/getCustomerRide/{username}")
    public Ride getCustomerRide(@PathVariable String username){
        Customer customer = (Customer) OnDriverSystem.getSystem().getUserList().get(username);
        Ride tempRide = new Ride(null,customer.getRide().getSource(),customer.getRide().getDestination(),customer.getRide().getPassenger_number());
        return tempRide;
    }

}
