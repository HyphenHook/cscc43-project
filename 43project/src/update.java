import java.sql.*;
import java.time.LocalDate;

public class update {

    private String jdbcUrl;

    private String username;

    private String userpassword;

    public update(String jdbcUrl, String username, String userpassword){
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.userpassword = userpassword;
    }
    public void addAccount(String name, String address, String birthdate, String occupation, String type, int SIN,
                           String email, String password){

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
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

    public void deleteAccount(int userID) {

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
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



    public void createListing(int userID, String type, double latitude, double longitude, String postalcode,
                              String address, String city, String country ){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
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

            linkListingAndUser(userID, address);

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void linkListingAndUser (int userID, String address){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
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


    //Insert availability for a listing in a continuous group of days
    public void insertAvailability(int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                   int endday, int price){

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
            String sqlQuery = "INSERT INTO Availability (listingID, a_date, price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);


            LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
            LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                preparedStatement.setInt(1, listingID);
                preparedStatement.setDate(2, java.sql.Date.valueOf(currentDate));
                preparedStatement.setDouble(3, price);
                preparedStatement.executeUpdate();

                currentDate = currentDate.plusDays(1);
            }
            System.out.println("Succussfully upload the availability!");
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Remove availability for a listing in a continuous group of days
    public void removeAvailability(int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                   int endday){

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
            String sqlQuery = "DELETE FROM Availability WHERE listingID = ? AND a_date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);


            LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
            LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

            preparedStatement.setInt(1, listingID);
            preparedStatement.setDate(2, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(endDate));

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Availability of listing with ID " + listingID + " from " +startDate.toString()
                + " to " + endDate.toString() + " has been removed.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAvailability(int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                  int endday){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
            String sqlQuery = "SELECT COUNT(*) AS count FROM Availability Where listingID = ? AND a_date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
            LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

            int total = 0;
            LocalDate Date = startDate;
            while (!Date.isAfter(endDate)) {
               Date = Date.plusDays(1);
               total++;
            }

            preparedStatement.setInt(1, listingID);
            preparedStatement.setDate(2, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count == total) {
                    System.out.println("Listing with ID" + listingID + "Available!");
                    preparedStatement.close();
                    return true;
                } else {
                    System.out.println("Unavailable");
                    preparedStatement.close();
                    return false;
                }
            }

            System.out.println("Error");
            preparedStatement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void booking(int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                        int endday, int userID){
        if(checkAvailability(listingID, startyear, startmonth, startday, endyear, endmonth, endday)){
            removeAvailability(listingID, startyear, startmonth, startday, endyear, endmonth, endday);

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, userpassword)) {
                String sqlQuery = "INSERT INTO The_Bookings (userID, listingID, a_date) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
                LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    preparedStatement.setInt(1, userID);
                    preparedStatement.setInt(2, listingID);
                    preparedStatement.setDate(3, java.sql.Date.valueOf(currentDate));
                    preparedStatement.executeUpdate();
                    currentDate = currentDate.plusDays(1);
                }
                System.out.println("Succussfully book listing with ID " + listingID + " from " +
                        startDate.toString() + " to " + endDate.toString());
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }



}
