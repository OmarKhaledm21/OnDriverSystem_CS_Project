import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DB_Helper {
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
                "Type TEXT CHECK (Type IN ('Driver','Customer','Admin')) NOT NULL DEFAULT 'Customer'," +
                "Status INT DEFAULT 0" +
                ");";

        String createDriverTable = "CREATE TABLE Driver (" +
                "UserName TEXT PRIMARY KEY," +
                "NationalID TEXT NOT NULL," +
                "LicenceNumber TEXT NOT NULL," +
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

            if(user instanceof Driver){
                //TODO ADD DETAILS TO DRIVER TABLE (OMAR ATEF)
             }else{
                //TODO ADD DETAULS FOR CUSTOMER (OMAR ATEF)
            }

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    //TODO PETER
    public boolean userExist(){
        return true;
    }

    public static void main(String[] args) {
        DB_Helper db_helper = new DB_Helper();
    }

}




