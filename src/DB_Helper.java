import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DB_Helper implements IDataBase{
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


        } catch (Exception e) { }
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

        String createDriverTable = "CREATE TABLE Driver (" +
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
        } catch (SQLException yeet) {
            yeet.printStackTrace();
        }
    }

    public ArrayList<User> selectAll(){
        ArrayList<User> db_List = new ArrayList<>();
        String selection = "SELECT UserName FROM Users;";
        try {
            ResultSet resultSet = statement.executeQuery(selection);
            while (resultSet.next()){
                String userName = resultSet.getString("UserName");
                User user = search(userName);
                if((user!=null)){
                    db_List.add(user);
                }
            }
        }catch (Exception e){}

        return db_List;
    }

    public boolean addUser(User user){
        String addQuery = "INSERT INTO Users(UserName, PassWord, BirthDate, Email, MobileNumber, Type, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            String userType = user.getClass().getName();

            preparedStatement = connection.prepareStatement(addQuery);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setDate(3,user.getBirthDay());
            preparedStatement.setString(4,user.getEmail());
            preparedStatement.setString(5,user.getMobileNumber());
            preparedStatement.setString(6,userType);
            preparedStatement.setInt(7,user.getStatus());

            if (user instanceof Captain) {
                //TODO ADD DETAILS TO DRIVER TABLE (OMAR ATEF)
                addToDriverTable((Captain) user);
            } else {
                //TODO ADD DETAULS FOR CUSTOMER (OMAR ATEF)
                addToCustomersTable(user);
            }

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean activateUser(User user){
        if(userExist(user)) {
            String activatedQuery = "UPDATE Users SET Status = 1 WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(activatedQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteUser(User user){
        if(userExist(user)) {
            String deleteUserQuery = "DELETE FROM Users WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                return false;
            }
            return true;
        }else {
            return false;
        }
    }

    public User search(String username){
        User user = null;
        try {
            String usersQueryString = "SELECT * FROM Users WHERE UserName = ?";
            PreparedStatement usersQuery = connection.prepareStatement(usersQueryString);

            usersQuery.setString(1, username);
            ResultSet userSet = usersQuery.executeQuery();

            if(userSet.next()){

                int userStatus = userSet.getInt("Status");
                String userType = userSet.getString("Type");
                String password = userSet.getString("PassWord");
                String email = userSet.getString("Email");
                String mobileNumber = userSet.getString("MobileNumber");

                if(userType.equals("Captain")){
                    String driverQueryString = "SELECT * FROM Driver WHERE UserName = ?";
                    PreparedStatement driversQuery = connection.prepareStatement(driverQueryString);

                    driversQuery.setString(1, username);

                    ResultSet driverSet = driversQuery.executeQuery();
                    String nationalID = driverSet.getString("NationalID");
                    String licenseNumber = driverSet.getString("LicenseNumber");

                    user = new Captain(username, password, email, mobileNumber, nationalID, licenseNumber,userStatus);

                }else if(userType.equals("Customer")){
                    user = new Customer(username, password, email, mobileNumber,userStatus);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return user;
    }

    public boolean addToDriverTable(Captain driver) {
        String query = "INSERT INTO Driver(UserName,NationalId,LicenseNumber,AverageRating)" +
                "VALUES(?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, driver.getUsername());
            preparedStatement.setString(2, driver.getNationalID());
            preparedStatement.setString(3, driver.getLicenseNumber());
            preparedStatement.setDouble(4,driver.getAverageRating());
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

    public boolean userExist(User user){
        if(user==null){
            return false;
        }
        return ( search(user.getUsername()) != null );
    }

    @Override
    public boolean suspendUser(User user) {
        if(userExist(user)) {
            String suspendQuery = "UPDATE Users SET Status = 0 WHERE UserName = ? ;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(suspendQuery);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        DB_Helper db_helper = new DB_Helper();
    }

}




