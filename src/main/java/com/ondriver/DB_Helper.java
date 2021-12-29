package com.ondriver;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DB_Helper implements IDataBase {
    private static Connection connection;
    private final String url = "jdbc:sqlite:";
    private final String dbPath = "db/OnDriver.db";
    private static Statement statement;

    public DB_Helper() {
        try {
            File file = new File(this.dbPath);
            boolean exist = file.exists();
            connection = DriverManager.getConnection(this.url + this.dbPath);
            statement = connection.createStatement();
            if (!exist) {
                CreateDB();
                System.out.println("Database Created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateDB() {
        String createUsersTable = "CREATE TABLE Users (" +
                "UserName TEXT PRIMARY KEY," +
                "PassWord TEXT NOT NULL," +
                "BirthDate TEXT," +
                "Email TEXT NOT NULL," +
                "MobileNumber TEXT NOT NULL," +
                "Type TEXT CHECK (Type IN ('Captain','Customer','Admin')) NOT NULL DEFAULT 'Customer'," +
                "Status INT DEFAULT 0" +
                ");";

        String createDriverTable = "CREATE TABLE Captain (" +
                "UserName TEXT PRIMARY KEY," +
                "NationalID TEXT NOT NULL," +
                "LicenseNumber TEXT NOT NULL," +
                "AverageRating REAL DEFAULT 0.0," +
                "CurrentLocation TEXT," +
                "FOREIGN KEY (UserName) REFERENCES Users (UserName) ON DELETE CASCADE" +
                ");";

        String createCustomerTable = "CREATE TABLE Customers (" +
                "UserName TEXT PRIMARY KEY," +
                "NewUser INT DEFAULT 1," +
                "FOREIGN KEY (UserName) REFERENCES Users (UserName) ON DELETE CASCADE" +
                ");";

        String areaTable = "CREATE TABLE Areas(" +
                "Location TEXT PRIMARY KEY NOT NULL" +
                ");";

        String createRidesTable = "CREATE TABLE Rides(" +
                "RideID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Source TEXT NOT NULL," +
                "Destination TEXT NOT NULL," +
                "RideStatus TEXT NOT NULL," +
                "Price REAL," +
                "CustomerUsername TEXT NOT NULL," +
                "CaptainUsername TEXT NOT NULL," +
                "Rating INT DEFAULT 0," +
                "Passengers INT DEFAULT 1," +
                "FOREIGN KEY (CustomerUsername) REFERENCES Users (UserName)," +
                "FOREIGN KEY (CaptainUsername) REFERENCES Users (UserName)" +
                "FOREIGN KEY (Source) REFERENCES Rides (Location),"+
                "FOREIGN KEY (Destination) REFERENCES Rides (Location)"+
                ");";

        String createLogsTable = "CREATE TABLE Logs (" +
                "EventName TEXT NOT NULL," +
                "EventTime TEXT," +
                "Log TEXT NOT NULL," +
                "RideID INT NOT NULL," +
                "FOREIGN KEY (RideID) REFERENCES Rides (RideID)" +
                ");";

        String createAdminTable = "CREATE TABLE Admin (" +
                "UserName TEXT PRIMARY KEY," +
                "FOREIGN KEY (UserName) REFERENCES Users (UserName)" +
                ");";

        String createFavArea = "CREATE TABLE CaptainFavArea(" +
                "CaptainUsername TEXT NOT NULL," +
                "Area TEXT NOT NULL," +
                "FOREIGN KEY (CaptainUsername) REFERENCES Users (UserName)," +
                "FOREIGN KEY (Area) REFERENCES Areas (Location)," +
                "PRIMARY KEY (CaptainUsername, Area)" +
                ");";

        String insertAdmin = "INSERT INTO Users(UserName,PassWord,Email,MobileNumber,Type,Status) " +
                "VALUES ('admin','admin','admin','admin','Admin',1);";

        String addAdmin = "INSERT INTO Admin (UserName) VALUES ('admin')";

        String holidaysTable = "CREATE TABLE Holidays(" +
                "MonthDay TEXT PRIMARY KEY," +
                "Discount REAL DEFAULT 0.05" +
                ");";

        String insertHolidays = "INSERT INTO Holidays (MonthDay,Discount) VALUES ('25/12',0.05) ;";
        try {
            statement.execute(createUsersTable);
            statement.execute(createDriverTable);
            statement.execute(createCustomerTable);
            statement.execute(createAdminTable);
            statement.execute(insertAdmin);
            statement.execute(addAdmin);
            statement.execute(createRidesTable);
            statement.execute(createLogsTable);
            statement.execute(areaTable);
            statement.execute(createFavArea);
            statement.execute(holidaysTable);
            statement.execute(insertHolidays);

        } catch (SQLException yeet) {
            yeet.printStackTrace();
        }
    }

    ////////////////////////////// Users Controller ////////////////////////////////////////
    public ArrayList<User> selectAll() {
        ArrayList<User> db_List = new ArrayList<>();
        String selectionQuery = "SELECT UserName FROM Users;";
        try {
            ResultSet resultSet = statement.executeQuery(selectionQuery);
            while (resultSet.next()) {
                String userName = resultSet.getString("UserName");
                User user = search(userName);
                if ((user != null)) {
                    db_List.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db_List;
    }

    public boolean addUser(User user) {
        String addQuery = "INSERT INTO Users(UserName, PassWord, BirthDate, Email, MobileNumber, Type, Status) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try {
            String userType = user.getClass().getSimpleName();
            PreparedStatement preparedStatement = connection.prepareStatement(addQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getBirthDay());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getMobileNumber());
            preparedStatement.setString(6, userType);
            preparedStatement.setInt(7, user.getStatus());

            if (user instanceof Captain)
                addToDriverTable((Captain) user);
            else
                addToCustomersTable(user);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean activateUser(User user) {
        if (userExist(user)) {
            String activationQuery = "UPDATE Users SET Status = 1 WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(activationQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else
            return false;
    }

    public boolean deleteUser(User user) {
        if (userExist(user)) {
            String deleteQuery = "DELETE FROM Users WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
                if(user instanceof Captain){
                    deleteCaptain(user.getUsername());
                }else{
                    deleteCustomer(user.getUsername());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else
            return false;
    }

    public boolean deleteCaptain(String username){
        String query = "DELETE FROM Captain WHERE UserName = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteCustomer(String username){
        String query = "DELETE FROM Customers WHERE UserName = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public User search(String username) {
        User user = null;
        try {
            String usersQueryString = "SELECT * FROM Users WHERE UserName = ?";
            PreparedStatement usersQuery = connection.prepareStatement(usersQueryString);

            usersQuery.setString(1, username);
            ResultSet userSet = usersQuery.executeQuery();

            if (userSet.next()) {

                int userStatus = userSet.getInt("Status");
                String userType = userSet.getString("Type");
                String password = userSet.getString("PassWord");
                String email = userSet.getString("Email");
                String mobileNumber = userSet.getString("MobileNumber");
                String birthday = userSet.getString("BirthDate");

                if (userType.equals("Captain")) {
                    String driverQueryString = "SELECT * FROM Captain WHERE UserName = ?";
                    PreparedStatement driversQuery = connection.prepareStatement(driverQueryString);

                    driversQuery.setString(1, username);

                    ResultSet driverSet = driversQuery.executeQuery();
                    String nationalID = driverSet.getString("NationalID");
                    String licenseNumber = driverSet.getString("LicenseNumber");
                    String currentLocation = driverSet.getString("CurrentLocation");
                    Area currLocation = new Area(currentLocation);
                    user = new Captain(username, password, email, mobileNumber, nationalID, licenseNumber, currLocation, userStatus);

                } else if (userType.equals("Customer")) {
                    user = new Customer(username, password, email, mobileNumber, userStatus, birthday);
                } else if (userType.equals("Admin")) {
                    user = new Admin(username, password, email, mobileNumber);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    public boolean addToDriverTable(Captain driver) {
        String query = "INSERT INTO Captain(UserName,NationalId,LicenseNumber,AverageRating,CurrentLocation)" +
                "VALUES(?,?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, driver.getUsername());
            preparedStatement.setString(2, driver.getNationalID());
            preparedStatement.setString(3, driver.getLicenseNumber());
            preparedStatement.setDouble(4, driver.getAverageRating());
            preparedStatement.setString(5, driver.getCurrentLocation().toString());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Driver Not Added.."); // Just for Debugging
            return false;
        }
        return true;
    }

    public boolean addToCustomersTable(User user) {
        String query = "INSERT INTO Customers(UserName) VALUES(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Customer Not Added"); // Just for Debugging
            return false;
        }
        return true;
    }

    public boolean userExist(User user) {
        if (user == null) {
            return false;
        }
        return (search(user.getUsername()) != null);
    }

    @Override
    public void updateCaptain(Captain captain) {
        // username national id lis avg rating
        String updateQuery = "UPDATE Captain SET NationalID = ?, LicenseNumber = ?, AverageRating = ? WHERE UserName = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, captain.getNationalID());
            preparedStatement.setString(2, captain.getLicenseNumber());
            preparedStatement.setDouble(3, captain.getAverageRating());
            preparedStatement.setString(4, captain.getUsername());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Couldn't update Driver: " + e.getMessage());
        }

    }

    @Override
    public boolean suspendUser(User user) {
        if (userExist(user)) {
            String suspendQuery = "UPDATE Users SET Status = 0 WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(suspendQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    ////////////////////////////// Rides Controller ////////////////////////////////////////
    @Override
    public void addRide(Ride ride) {
        String addRideQuery = "INSERT INTO Rides (Source,Destination,RideStatus,Price,CustomerUsername,CaptainUsername,Rating,Passengers)"
                + "VALUES(?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(addRideQuery);
            preparedStatement.setString(1, ride.getSource().getLocation());
            preparedStatement.setString(2, ride.getDestination().getLocation());
            preparedStatement.setString(3, ride.getRideStatus().toString());
            preparedStatement.setDouble(4, ride.getPrice());
            preparedStatement.setString(5, ride.getCustomer().getUsername());
            preparedStatement.setString(6, ride.getDriver().getUsername());
            preparedStatement.setInt(7, ride.getRating());
            preparedStatement.setInt(8,ride.getPassenger_number());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Ride searchRide(int rideID) {
        Ride res = null;
        String selectRideQuery = "SELECT * FROM Rides WHERE RideID = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectRideQuery);
            preparedStatement.setInt(1, rideID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Customer tempCustomer = (Customer) search(resultSet.getString("CustomerUsername"));
                Captain tempCaptain = (Captain) search(resultSet.getString("CaptainUsername"));
                Area tempSource = searchArea(resultSet.getString("Source"));
                Area tempDestination = searchArea(resultSet.getString("Destination"));
                int passenger_num = resultSet.getInt("Passengers");
                res = new Ride(tempCustomer, tempSource, tempDestination,passenger_num);
            } else {
                System.out.println("This ride doesn't exist!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // TODO add ride counter.....
    public int rideCounter() {
        int counter = 0;
        String query = "SELECT * FROM Rides";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    @Override
    public void changeRideStatus(Ride ride) {
        String updateStatus = "UPDATE Rides SET RideStatus = ? , Rating = ? , Price = ? WHERE CustomerUsername = ? AND RideID = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatus);
            preparedStatement.setString(1, ride.getRideStatus().toString());
            preparedStatement.setInt(2, ride.getRating());
            preparedStatement.setDouble(3,ride.getPrice());
            preparedStatement.setString(4, ride.getCustomer().getUsername());
            preparedStatement.setInt(5, ride.getID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////// Area Controller ////////////////////////////////////////
    @Override
    public void addArea(Area area) {
        if (searchArea(area.getLocation()) == null) {
            String addAreaQuery = "INSERT INTO Areas VALUES(?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(addAreaQuery);
                preparedStatement.setString(1, area.getLocation());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Area searchArea(String location) {
        String query = "SELECT * FROM Areas WHERE Location = ?";
        Area area = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, location);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                area = new Area(resultSet.getString("Location"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    @Override
    public void driverMoved(Captain captain) {
        String query = "UPDATE Captain SET CurrentLocation = ? WHERE UserName = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, captain.getCurrentLocation().toString());
            preparedStatement.setString(2, captain.getUsername());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////// Events Controller ////////////////////////////////////////

    @Override
    public ArrayList<RideEvent> getEvents(Ride ride) {
        ArrayList<RideEvent> events = new ArrayList<>();
        String query = "SELECT * FROM Logs Where RideID = ?";
        int rideID = ride.getID();
        RideEvent rideEvent = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rideID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String eventType = resultSet.getString("EventName");

                switch (eventType) {
                    case "CustomerAcceptedEvent":
                        rideEvent = new CustomerAcceptedEvent(ride);
                        break;
                    case "DriverArrivedEvent":
                        rideEvent = new DriverArrivedEvent(ride);
                        break;
                    case "OfferPriceEvent": //TODO: attach offer to event
                        
                        rideEvent = new OfferPriceEvent(ride, ride.getPriceOffers().get(ride.getPriceOffers().size() - 1));
                        break;
                    case "RideEndedEvent":
                        rideEvent = new RideEndedEvent(ride);
                        break;
                }

                if (rideEvent != null) {
                    System.out.println(rideEvent.toString());
                    events.add(rideEvent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public void saveEvent(RideEvent log) {
        Ride ride = log.getRide();

        String logQuery = "INSERT INTO Logs (EventName,Log,RideID) VALUES(?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(logQuery);
            preparedStatement.setString(1, log.getClass().getSimpleName());
            preparedStatement.setString(2, log.toString());
            preparedStatement.setInt(3, ride.getID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> searchLogs(int RideID) {
        ArrayList<String>logs = new ArrayList<>();
        try {
            String query = "SELECT Log FROM Logs WHERE RideId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,RideID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                logs.add(resultSet.getString("Log"));
            }
        }catch (Exception e){
//            System.out.println("Ride Not found");
            logs=null;
        }
        return logs;
    }

    public ArrayList<Ride> getRidesHistory(User user){
        String historyQuery = "SELECT * FROM Rides WHERE CaptainUsername = ?;";
        ArrayList<Ride> ridesHistory = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(historyQuery);
            preparedStatement.setString(1,user.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Ride ride = null;
                Customer customer = (Customer) search(resultSet.getString("CustomerUsername"));
                Captain captain = (Captain) user;
                int rating = resultSet.getInt("Rating");
                double price = resultSet.getDouble("Price");
                Area source = searchArea( resultSet.getString("Source") );
                Area destination = searchArea( resultSet.getString("Destination") );
                int passenger_number = resultSet.getInt("Passengers");
                ride = new Ride(customer,source,destination,passenger_number);
                ride.setRating(rating);
                ride.setRideStatus(RideStatus.FINISHED);
                ridesHistory.add(ride);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ridesHistory;
    }

    @Override
    public ArrayList<Area> getAreas() {
        String query = "SELECT * FROM Areas;";
        ArrayList<Area> areas = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Area area = new Area(resultSet.getString("Location"));
                areas.add(area);
            }
        }catch (Exception e){}

        return areas;
    }

    @Override
    public boolean isNewUser(String username) {
        String query = "SELECT * FROM Customers WHERE UserName = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("NewUser") == 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateCustomerRides(String username) {
        String query = "UPDATE Customers SET NewUser = 0 WHERE UserName = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public double checkHoliday(String date) {
        String query = "SELECT * FROM Holidays WHERE MonthDay = ?;";
        double discount = 0.0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                discount = resultSet.getDouble("Discount");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return discount;
    }

    public static void main(String[] args) {
        Area source = new Area("a1");
        Area destination = new Area("b2");
        Area wrong = new Area("a1");
        DB_Helper db_helper = new DB_Helper();
        Customer customer = new Customer("o1", "o1", "o1", "o1", 1, "25/12");
        Captain captain = new Captain("d1", "d1", "d1", "d1", "d1", "d1", destination, 1);

        Ride ride = Ride.createRide(customer, source, destination,2);
        Offer offer = new Offer(captain, 0, ride);
        ride.setDriver(captain);
        db_helper.addUser(customer);
        db_helper.addUser(captain);
        db_helper.addArea(source);
        db_helper.addArea(destination);
        db_helper.addArea(wrong);
        db_helper.addRide(ride);

        System.out.println(db_helper.rideCounter());
        // db_helper.deleteNotification(1);
        // db_helper.addRide(ride);
        // Ride temp = db_helper.searchRide(ride.getID());
        // System.out.println(temp.toString());

         RideEvent rideEvent = new CustomerAcceptedEvent(ride);
         RideEvent rideEvent1 = new RideEndedEvent(ride);
         db_helper.saveEvent(rideEvent);
         db_helper.saveEvent(rideEvent1);
         db_helper.searchLogs(1);


        // db_helper.getEvent(ride);
    }


}
