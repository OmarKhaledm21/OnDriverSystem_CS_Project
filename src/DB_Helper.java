import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DB_Helper implements IDataBase {
    private static Connection connection;
    private final String url = "jdbc:sqlite:";
    private final String dbPath = "db/OnDriver.db";
    private static Statement statement;

    DB_Helper() {
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
        }
    }

    public void CreateDB() {
        String createUsersTable = "CREATE TABLE Users (" +
                "UserName TEXT PRIMARY KEY," +
                "PassWord TEXT NOT NULL," +
                "BirthDate DATE," +
                "Email TEXT NOT NULL," +
                "MobileNumber TEXT NOT NULL," +
                "Type TEXT CHECK (Type IN ('Captain','Customer','Admin')) NOT NULL DEFAULT 'Customer'," +
                "Status INT DEFAULT 0" +
                ");";

        String createDriverTable = "CREATE TABLE Drivers (" +
                "UserName TEXT PRIMARY KEY," +
                "NationalID TEXT NOT NULL," +
                "LicenseNumber TEXT NOT NULL," +
                "AverageRating REAL DEFAULT 0.0," +
                "FOREIGN KEY (UserName) REFERENCES Users (UserName)" +
                ");";

        String createCustomerTable = "CREATE TABLE Customers (" +
                "UserName TEXT PRIMARY KEY," +
                "FOREIGN KEY (UserName) REFERENCES Users (UserName)" +
                ");";
        //TODO
        String areaTable = "CREATE TABLE Area(" +
                "Location TEXT PRIMARY KEY NOT NULL" +
                ");";

        String createRidesTable = "CREATE TABLE Rides(" +
                "RideID INTEGER PRIMARY KEY," +
                "Source TEXT NOT NULL," +
                "Destination TEXT NOT NULL," +
                "RideStatus TEXT NOT NULL," +
                "Price REAL," +
                "Customer_Username TEXT NOT NULL," +
                "Captain_Username TEXT NOT NULL," +
                "Rating INT DEFAULT 0," +
                "FOREIGN KEY (Customer_Username) REFERENCES Users (UserName)," +
                "FOREIGN KEY (Captain_Username) REFERENCES Users (UserName)" +
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

        String insertAdmin = "INSERT INTO Users(UserName,PassWord,Email,MobileNumber,Type,Status) " +
                "VALUES ('admin','admin','admin','admin','Admin',1);";

        String addAdmin = "INSERT INTO Admin (UserName) VALUES ('admin')";

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
        } catch (SQLException yeet) {
            yeet.printStackTrace();
        }
    }

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
            String userType = user.getClass().getName();
            PreparedStatement preparedStatement = connection.prepareStatement(addQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, user.getBirthDay());
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
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else
            return false;
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

                if (userType.equals("Captain")) {
                    String driverQueryString = "SELECT * FROM Drivers WHERE UserName = ?";
                    PreparedStatement driversQuery = connection.prepareStatement(driverQueryString);

                    driversQuery.setString(1, username);

                    ResultSet driverSet = driversQuery.executeQuery();
                    String nationalID = driverSet.getString("NationalID");
                    String licenseNumber = driverSet.getString("LicenseNumber");

                    user = new Captain(username, password, email, mobileNumber, nationalID, licenseNumber, userStatus);

                } else if (userType.equals("Customer")) {
                    user = new Customer(username, password, email, mobileNumber, userStatus);
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
        String query = "INSERT INTO Drivers(UserName,NationalId,LicenseNumber,AverageRating)" +
                "VALUES(?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, driver.getUsername());
            preparedStatement.setString(2, driver.getNationalID());
            preparedStatement.setString(3, driver.getLicenseNumber());
            preparedStatement.setDouble(4, driver.getAverageRating());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Driver Not Added.."); //Just for Debugging
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
            System.out.println("Customer Not Added");  //Just for Debugging
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

    @Override
    public void addRide(Ride ride) {
        String addRideQuery = "INSERT INTO Rides (Source,Destination,RideStatus,Price,Customer_Username,Captain_Username,Rating)" +
                "VALUES(?,?,?,?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(addRideQuery);
            preparedStatement.setString(1, ride.getSource().getLocation());
            preparedStatement.setString(2, ride.getDestination().getLocation());
            preparedStatement.setString(3, ride.getRideStatus().toString());
            preparedStatement.setDouble(4, ride.getPrice());
            preparedStatement.setString(5, ride.getCustomer().getUsername());
            preparedStatement.setString(6, ride.getDriver().getUsername());
            preparedStatement.setInt(7, ride.getRating());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //TODO add ride counter.....
    @Override
    public void changeRideStatus(Ride ride) {
        String updateStatus = "UPDATE Rides SET RideStatus = ? , Rating = ? WHERE Customer_Username = ? AND RideID = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatus);
            preparedStatement.setString(1, ride.getRideStatus().toString());
            preparedStatement.setInt(2, ride.getRating());
            preparedStatement.setString(3, ride.getCustomer().getUsername());
            preparedStatement.setInt(4, ride.getID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAreaDB(Area area) {
        if (!SearchArea(area)){
        String addAreaQuery = "INSERT INTO Area VALUES(?)";
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
    public boolean SearchArea(Area area) {
        String query = "SELECT * FROM Area WHERE Location = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, area.getLocation());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String loc = resultSet.getString("Location");
                return true;
            } else {
               return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void getEvent(Ride ride) {
        String query = "SELECT * FROM Logs Where RideID = ?";
        int rideID = ride.getID();
        RideEvent rideEvent = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rideID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("EventName").equals("CustomerAcceptedEvent")) {
                    rideEvent = new CustomerAcceptedEvent(ride);
                } else if (resultSet.getString("EventName").equals("DriverArrivedEvent")) {
                    rideEvent = new DriverArrivedEvent(ride);
                } else if (resultSet.getString("EventName").equals("OfferPriceEvent")) {
                    rideEvent = new OfferPriceEvent(ride, ride.getPriceOffers().get(ride.getPriceOffers().size() - 1));
                } else if (resultSet.getString("EventName").equals("RideEndedEvent")) {
                    rideEvent = new RideEndedEvent(ride);
                }
                if (rideEvent != null) {
                    System.out.println(rideEvent.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEvent(RideEvent log) {
        Ride ride = log.getRide();

        String logQuery = "INSERT INTO Logs (EventName,Log,RideID) VALUES(?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(logQuery);
            preparedStatement.setString(1, log.getClass().getName());
            preparedStatement.setString(2, log.toString());
            preparedStatement.setInt(3, ride.getID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ride searchRide(Ride ride) {
        Ride res = null;
        String selectRideQuery = "SELECT * FROM Rides WHERE RideID = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectRideQuery);
            preparedStatement.setInt(1, ride.getID());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Customer tempCustomer = (Customer) search(resultSet.getString("Customer_Username"));
                Captain tempCaptain = (Captain) search(resultSet.getString("Captain_Username"));
                Area tempSource = searchArea(resultSet.getString("Source"));
                Area tempDestination = searchArea(resultSet.getString("Destination"));
                res = new Ride(tempCustomer, tempSource, tempDestination);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return res;
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
                Customer tempCustomer = (Customer) search(resultSet.getString("Customer_Username"));
                Captain tempCaptain = (Captain) search(resultSet.getString("Captain_Username"));
                Area tempSource = searchArea(resultSet.getString("Source"));
                Area tempDestination = searchArea(resultSet.getString("Destination"));
                res = new Ride(tempCustomer, tempSource, tempDestination);
            } else {
                System.out.println("This ride doesn't exist!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Area searchArea(String location) {
        String query = "SELECT * FROM Area WHERE Location = ?";
        Area area = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, location);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                area = new Area(resultSet.getString("Location"));
            } else {
                System.out.println("Area not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    public static void main(String[] args) {
        DB_Helper db_helper = new DB_Helper();
        Customer customer = new Customer("o1", "o1", "o1", "o1", 1);
        Captain captain = new Captain("d1", "d1", "d1", "d1", "d1", "d1", 1);
        Area source = new Area("a1");
        Area destination = new Area("b2");
        Area wrong = new Area("a1");
        Ride ride = Ride.createRide(customer, source, destination);
        ride.setDriver(captain);
        db_helper.addUser(customer);
        db_helper.addUser(captain);
        db_helper.addAreaDB(source);
        db_helper.addAreaDB(destination);
        db_helper.addAreaDB(wrong);
        //db_helper.addRide(ride);
        //Ride temp = db_helper.searchRide(ride.getID());
        //System.out.println(temp.toString());

        RideEvent rideEvent = new CustomerAcceptedEvent(ride);
        RideEvent rideEvent1 = new RideEndedEvent(ride);
        db_helper.saveEvent(rideEvent);
        db_helper.saveEvent(rideEvent1);

        db_helper.getEvent(ride);
    }

}




