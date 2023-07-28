// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
        String username = "root";
        String password = "204x78y69";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM Student"; // Replace with your actual table name

            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Process the result set
            while (resultSet.next()) {
                // Access data from the result set
                int id = resultSet.getInt("sID");
                String first_name = resultSet.getString("firstName");
                String last_name = resultSet.getString("surName");
                // Continue extracting other columns as needed
                System.out.println("ID: " + id + ", Name: " + first_name + " " + last_name);
            }

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}