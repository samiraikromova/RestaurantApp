import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/RestaurantApp"; 
    private static final String USER = "root";     
private static final String PASSWORD = "ikromova2006"; 

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    public static boolean validateUser(String username, String password) {
        // Assuming you have a table `users` with columns `username` and `password`
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();  // If resultSet has data, user is valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Error in DB query
        }
    }
}
