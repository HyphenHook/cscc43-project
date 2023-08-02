import java.sql.*;
import java.time.LocalDate;

public class update {

    public void addAccount(connectionSQL c, String name, String address, String birthdate, String occupation, String type, int SIN,
                           String email, String password){

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "INSERT INTO User(name, address, birthdate, occupation, type, SIN, email, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name); // Set the value of 'name' using the variable myName
            preparedStatement.setString(2, address); // Example: Set the address directly
            preparedStatement.setDate(3, java.sql.Date.valueOf(birthdate)); // Example: Set birthdate using a string date
            preparedStatement.setString(4, occupation); // Example: Set occupation directly
            preparedStatement.setString(5, type); // Example: Set type directly
            preparedStatement.setInt(6, SIN); // Example: Set SIN directly
            preparedStatement.setString(7, email); // Example: Set email directly
            preparedStatement.setString(8, password); // Example: Set password directly
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 1) {
                System.out.println("Account created successfully.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteAccount(connectionSQL c, int userID) {

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "DELETE FROM User Where userID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userID);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted == 1) {
                System.out.println("Account" + userID + "has been deleted.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String AccountType(connectionSQL c, int userID) {
        String result = "false";
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)){
            String sqlQuery = "SELECT type FROM User Where userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String type = resultSet.getString("type");

                if (type.equals("renter")) {
                    result = "renter";
                    System.out.println("user with ID "+ userID + " is a renter.");
                    preparedStatement.close();;
                    return result;
                } else {
                    result = "host";
                    System.out.println("user with ID "+ userID + " is a host.");
                    preparedStatement.close();
                    return result;
                }

            } else {
                System.out.println("User with ID " + userID + " not found.");
                return result;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("An error occur");
        return result;
    }



    public void createListing(connectionSQL c, int userID, String type, double latitude, double longitude, String postalcode,
                              String address, String city, String country ){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "INSERT INTO Listing(type, latitude, longitude, postalcode, address, city, country) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, type);
            preparedStatement.setDouble(2, latitude);
            preparedStatement.setDouble(3, longitude);
            preparedStatement.setString(4, postalcode);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, city);
            preparedStatement.setString(7, country);
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 1) {
                System.out.println("Listing created successfully.");
            }

            linkListingAndUser(c, userID, address);

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void linkListingAndUser (connectionSQL c, int userID, String address){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT listingID FROM Listing WHERE address = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, address);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int listingID = resultSet.getInt("listingID");
                String sqlQuery_2 = "INSERT INTO TheListings (userID, listingID) VALUES (?, ?)";
                PreparedStatement ps = connection.prepareStatement(sqlQuery_2);
                ps.setInt(1, userID);
                ps.setInt(2, listingID);
                int rowsInserted = ps.executeUpdate();

                if (rowsInserted == 1) {
                    System.out.println("User and Listing related successfully");
                } else {
                    System.out.println("relating process failed");
                }

                ps.close();

            } else {
                System.out.println("The related Listing not found.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int checkOwner(connectionSQL c, int listingID) {
        int result = -1;
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)){
            String sqlQuery = "SELECT userID FROM TheListings Where listingID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, listingID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("userID");
            } else {
                System.out.println("No listing with listingID " + listingID);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public void RemoveListing(connectionSQL c, int listingID) {
        Availability a = new Availability();
        if(a.removeAllAvailability(c, listingID)) {
            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {

                String sqlQuery1 = "DELETE FROM TheListings Where listingID = ?";

                PreparedStatement p1 = connection.prepareStatement(sqlQuery1);
                p1.setInt(1, listingID);
                int r1 = p1.executeUpdate();

                if (r1 == 1) {
                    System.out.println("Listing with ID " + listingID + " removed from TheListings");
                }

                p1.close();


                String sqlQuery3 = "DELETE FROM Listing Where listingID = ?";

                PreparedStatement p3 = connection.prepareStatement(sqlQuery3);
                p3.setInt(1, listingID);
                int r3 = p3.executeUpdate();

                if (r3 == 1) {
                    System.out.println("Listing with ID " + listingID + " removed from Listing");
                }

                p3.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to remove listing with listingID " + listingID + " because" +
                    "there are still bookings ongoing.");
        }
    }



}
