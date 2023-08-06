import java.sql.*;
import java.time.LocalDate;

public class update {

    public void addAccount(connectionSQL c, String name, String address, String birthdate, String occupation, String type,
                           String SIN, String email, String password){

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "INSERT INTO PersonalInfo(sin, name, address, birthdate, occupation) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, SIN);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setDate(4, java.sql.Date.valueOf(birthdate));
            preparedStatement.setString(5, occupation); // Example: Set occupation directly
            /*preparedStatement.setString(5, type); // Example: Set type directly
            preparedStatement.setString(1, SIN); // Example: Set SIN directly
            preparedStatement.setString(7, email); // Example: Set email directly
            preparedStatement.setString(8, password); // Example: Set password directly*/
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 1) {
                System.out.println("PersonalInfo inserted successfully! Next step, insert user account.");
                String query2 = "INSERT INTO User(type, sin, email, password) VALUES (?, ?, ?, ?)";

                PreparedStatement ps2 = connection.prepareStatement(query2);
                ps2.setString(1, type);
                ps2.setString(2, SIN);
                ps2.setString(3, email);
                ps2.setString(4, password);

                int result = ps2.executeUpdate();

                if(result == 1) {
                    System.out.println ("Successfully inserted user with email: " + email + " ; password: "
                            + password);
                }

                ps2.close();

            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Get the SIN of user
    private String getUserSin(connectionSQL c, int userID) {
        String sin = "";
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT sin FROM User WHERE userID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                sin = resultSet.getString("sin");

            } else {
                System.out.println("User with ID " + userID + " not found.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sin;
    }

    //Delete PersonalInfo with given sin
    private void deletePersonalInfo(connectionSQL c, String sin) {
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery_2 = "DELETE FROM PersonalInfo WHERE sin = ?";

            PreparedStatement ps = connection.prepareStatement(sqlQuery_2);
            ps.setString(1, sin);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted == 1) {
                System.out.println("PersonalInfo deleted successfully");
            } else {
                System.out.println("Deletion failed.");
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //Delete Personal Account with PersonalInfo
    public void deleteAccount(connectionSQL c, int userID) {
        String sin;
        if(!((sin = getUserSin(c, userID)).isEmpty())) {
            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
                String sqlQuery = "DELETE FROM User Where userID = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, userID);
                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted == 1) {
                    System.out.println("User with ID " + userID + " has been deleted.");
                    deletePersonalInfo(c, sin);
                }

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String AccountType(connectionSQL c, int userID) {
        String result = "";
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
            String sqlQuery = "INSERT INTO LocationInfo(latitude, longitude, postalcode, address, city, country) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setDouble(1, latitude);
            preparedStatement.setDouble(2, longitude);
            preparedStatement.setString(3, postalcode);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, city);
            preparedStatement.setString(6, country);
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 1) {
                System.out.println("LocationInfo created successfully! Next, insert listing.");
                String sql2 = "INSERT INTO Listing(type, address) VALUES (?, ?)";

                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ps2.setString(1, type);
                ps2.setString(2, address);
                int rowsInserted2 = ps2.executeUpdate();

                if (rowsInserted2 == 1) {
                    System.out.println("Listing created successfully! Finally, link listing and user.");
                    linkListingAndUser(c, userID, address);
                }
                ps2.close();
            }


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
            String sqlQuery = "SELECT userID FROM TheListings WHERE listingID = ?";
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

    private String getLocationAddress(connectionSQL c, int listingID) {
        String address = "";
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT address FROM Listing WHERE listingID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, listingID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                address = resultSet.getString("address");

            } else {
                System.out.println("Listing with ID " + listingID + " not found.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    private void RemoveLocationInfo(connectionSQL c, String address){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery_2 = "DELETE FROM LocationInfo WHERE address = ?";

            PreparedStatement ps = connection.prepareStatement(sqlQuery_2);
            ps.setString(1, address);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted == 1) {
                System.out.println("LocationInfo deleted successfully");
            } else {
                System.out.println("Deletion failed.");
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

                String address = getLocationAddress(c, listingID);


                String sqlQuery3 = "DELETE FROM Listing Where listingID = ?";

                PreparedStatement p3 = connection.prepareStatement(sqlQuery3);
                p3.setInt(1, listingID);
                int r3 = p3.executeUpdate();

                if (r3 == 1) {
                    System.out.println("Listing with ID " + listingID + " removed from Listing");
                    System.out.println("Now remove LocationInfo related to it.");
                    RemoveLocationInfo(c, address);
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

    // Given a large string containing amenities separated by
    public void insertAmenities(connectionSQL c, int listingID, String[] Amenities){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "INSERT INTO Amenities (name, listingID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            for(String amenity : Amenities) {
                preparedStatement.setString(1, amenity);
                preparedStatement.setInt(2, listingID);
                preparedStatement.executeUpdate();
            }
            System.out.println("Succussfully upload these amenities for listing " + listingID);
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
