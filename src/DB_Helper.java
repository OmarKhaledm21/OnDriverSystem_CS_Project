import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DB_Helper {
    private static Connection connection;
    private static String url = "jdbc:sqlite:";
    private String dbPath = "db/OnDriver.db";

    DB_Helper() {
        try {
            File file = new File(dbPath);
            boolean exist = file.exists();
            connection = DriverManager.getConnection(url + dbPath);

            if (!exist) {
                CreateDB();
            }
            System.out.println("DB_created");

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
                "Type TEXT CHECK (Type IN ('Driver','Customer','Admin')) NOT NULL DEFAULT 'Customer'" +
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
        String insertAdmin = "INSERT INTO Users(UserName,PassWord,Email,MobileNumber,Type) " +
                "VALUES ('admin','admin','admin','admin','Admin');";
        String addAdmin = "INSERT INTO Admin (UserName) VALUES ('admin')";
        try {
            Statement statement = connection.createStatement();
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

    public static void main(String[] args) {
        DB_Helper db_helper = new DB_Helper();
    }

}




