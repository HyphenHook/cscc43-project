import java.sql.*;
import java.time.LocalDate;

public class Availability {
    //Insert availability for a listing in a continuous group of days
    public void insertAvailability(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                   int endday, double price){

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "INSERT INTO Availability (listingID, a_date, price, status) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);


            LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
            LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                preparedStatement.setInt(1, listingID);
                preparedStatement.setDate(2, java.sql.Date.valueOf(currentDate));
                preparedStatement.setDouble(3, price);
                preparedStatement.setString(4, "no");
                preparedStatement.executeUpdate();

                currentDate = currentDate.plusDays(1);
            }
            System.out.println("Succussfully upload the availability!");
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeAvailability(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                   int endday, int type){
        if(checkAvailability(c, listingID, startyear, startmonth, startday, endyear, endmonth, endday, 1)) {
            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
                String sqlQuery = "UPDATE Availability SET status = ? WHERE listingID = ? AND a_date BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                String status = "no";
                if (type == 1) {
                    status = "yes";
                } else if (type == 2) {
                    status = "book";
                }

                LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
                LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, listingID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
                int result = preparedStatement.executeUpdate();

                if (result > 0) {
                    System.out.println("Availability of listing with ID " + listingID + " from " + startDate.toString()
                            + " to " + endDate.toString() + " has been changed to " + status);
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to change availability of listing with listing ID " + listingID
            + " because it has been reserved in that period");
        }
    }

    public boolean checkAvailability(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                     int endday, int mode){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT COUNT(*) AS count FROM Availability Where listingID = ? AND status = ? AND a_date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
            LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

            int total = 0;
            LocalDate Date = startDate;
            while (!Date.isAfter(endDate)) {
                Date = Date.plusDays(1);
                total++;
            }

            String status = "yes";
            if (mode == 1){
                status = "book";
            }

            preparedStatement.setInt(1, listingID);
            preparedStatement.setString(2, status);
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                //Case 1: check whether there exists dates that have been booked
                if(mode == 1){
                    if (count > 0) {
                        System.out.println("Listing with ID " + listingID + " not available to change");
                        preparedStatement.close();
                        return false;
                    } else {
                        System.out.println("Listing with ID " + listingID + " able to change");
                        preparedStatement.close();
                        return true;
                    }
                }
                //Case 2: Check whether these dates are able to be booked
                else {
                    if (count == total) {
                        System.out.println("Listing with ID " + listingID + " Available from " + startDate.toString() + " to "
                                + endDate.toString());
                        preparedStatement.close();
                        return true;
                    } else {
                        System.out.println("Unavailable from " + startDate.toString() + " to "
                                + endDate.toString());
                        preparedStatement.close();
                        return false;
                    }
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



    public void updateAvailabilityPrice(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                        int endday, double new_price){

        if(checkAvailability(c, listingID, startyear, startmonth, startday, endyear, endmonth, endday, 1)){

            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
                String sqlQuery = "UPDATE Availability SET price = ? WHERE listingID = ? AND a_date BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
                LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

                preparedStatement.setDouble(1, new_price);
                preparedStatement.setInt(2, listingID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
                int result = preparedStatement.executeUpdate();

                if(result > 0) {
                    System.out.println("Succussfully update listing with ID " + listingID + " from " +
                            startDate.toString() + " to " + endDate.toString() + " with new price " + new_price);
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("Can not change availability since it is reserved in the given period.");
        }
    }

    public void removeAvailability(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                                   int endday, int mode){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            if(checkAvailability(c, listingID, startyear, startmonth, startday, endyear, endmonth, endday, 1)){
                String sqlQuery = "DELETE FROM Availability Where listingID = ? AND a_date BETWEEN ? AND ?";

                LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
                LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

                PreparedStatement p = connection.prepareStatement(sqlQuery);
                p.setInt(1, listingID);
                p.setDate(2, java.sql.Date.valueOf(startDate));
                p.setDate(3, java.sql.Date.valueOf(endDate));

                int result = p.executeUpdate();

                if (result > 0) {
                    System.out.println("Listing with ID " + listingID + " remove Availability from " + startDate.toString()
                    + " to " + endDate.toString());
                }

                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkRemoveAllAvailability(connectionSQL c, int listingID){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT COUNT(*) AS count FROM Availability Where listingID = ? AND status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);


            preparedStatement.setInt(1, listingID);
            preparedStatement.setString(2, "book");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");

                if (count > 0) {
                    System.out.println("Cannot be removed since there are still ongoing bookings.");
                    preparedStatement.close();
                    return false;
                } else {
                    System.out.println("Able to be removed");
                    preparedStatement.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeAllAvailability(connectionSQL c, int listingID){
      if(checkRemoveAllAvailability(c, listingID)){
          try(Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)){
              String sqlQuery = "DELETE FROM Availability Where listingID = ?";

              PreparedStatement p = connection.prepareStatement(sqlQuery);
              p.setInt(1, listingID);
              int result = p.executeUpdate();

              if (result >= 0) {
                  System.out.println("Listing with ID " + listingID + " removed from Availability");
              }

              p.close();
              return true;
          } catch (SQLException e){
              e.printStackTrace();
          }
      } else {
          return false;
      }
      return false;
    }

    public void booking(connectionSQL c, int listingID, int startyear, int startmonth, int startday, int endyear, int endmonth,
                        int endday, int userID){
        if(checkAvailability(c, listingID, startyear, startmonth, startday, endyear, endmonth, endday, 0)){
            changeAvailability(c, listingID, startyear, startmonth, startday, endyear, endmonth, endday, 2);

            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
                String sqlQuery = "INSERT INTO TheBookings (renterID, listingID, startDate, endDate, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                LocalDate startDate = LocalDate.of(startyear, startmonth, startday);
                LocalDate endDate = LocalDate.of(endyear, endmonth, endday);

                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, listingID);
                preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
                preparedStatement.setString(5, "book");
                preparedStatement.executeUpdate();

                System.out.println("Succussfully book listing with ID " + listingID + " from " +
                        startDate.toString() + " to " + endDate.toString());
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    public int getBookingListing(connectionSQL c, int bookingID){
        int answer = -1;
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT listingID FROM TheBookings WHERE bookingID = ? and status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, bookingID);
            preparedStatement.setString(2, "book");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                answer = resultSet.getInt("listingID");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("The booking with ID " + bookingID + " books listing with ID " + answer);
        return answer;
    }

    private boolean checkRelation(connectionSQL c, int userID, int bookingID){
        update up = new update();
        boolean last = false;

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT renterID, listingID FROM TheBookings WHERE bookingID = ? and status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, bookingID);
            preparedStatement.setString(2, "book");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int renterID = resultSet.getInt("renterID");
                int listingID = resultSet.getInt("listingID");
                if(up.AccountType(c, userID).equals("renter")) {
                    if (userID == renterID) {
                        System.out.println("Booking with ID " + bookingID + " is booked by user with ID " + userID);
                        preparedStatement.close();
                        last = true;
                    } else {
                        System.out.println("Booking with ID " + bookingID + " not booked by this user.");
                        preparedStatement.close();
                    }
                } else if (up.AccountType(c, userID).equals("renter")){
                    int owner = up.checkOwner(c, listingID);
                    if (userID == owner){
                        System.out.println("Listing with ID " + listingID + " belongs to user with ID " + userID);
                        preparedStatement.close();
                        last = true;
                    } else {
                        System.out.println("Cannot cancel a booking whose listing does not belong to user " + userID);
                        preparedStatement.close();
                    }
                } else {
                    System.out.println("Unable to find this user.");
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return last;
    }

    // Specially used for recovering availability after cancellation.
    private void recoverAvailability(connectionSQL c, int listingID, java.sql.Date start, java.sql.Date end){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "UPDATE Availability SET status = ? WHERE listingID = ? AND a_date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);


            preparedStatement.setString(1, "yes");
            preparedStatement.setInt(2, listingID);
            preparedStatement.setDate(3, start);
            preparedStatement.setDate(4, end);
            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Listing with ID " + listingID + " has recovered from cancelling appointment from "
                + start.toString() + " to " + end.toString());
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Recover the availability from cancellation of booking to "yes" (Available for booking)
    private void recoverFromCancel(connectionSQL c, int bookingID){
        try(Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)){
            int listingID = getBookingListing(c, bookingID);
            System.out.println(listingID);
            String sqlQuery = "SELECT startDate, endDate FROM TheBookings WHERE bookingID = ? and status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, bookingID);
            preparedStatement.setString(2, "book");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                java.sql.Date start = resultSet.getDate("startDate");
                java.sql.Date end = resultSet.getDate("endDate");
                recoverAvailability(c, listingID, start, end);
            }

            preparedStatement.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void cancelBooking(connectionSQL c, int bookingID, int userID){
        if(checkRelation(c, userID, bookingID)){
            try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
                String sq = "UPDATE TheBookings SET status = ? WHERE bookingID = ?";
                PreparedStatement p = connection.prepareStatement(sq);
                recoverFromCancel(c, bookingID);

                p.setString(1, "cancelled");
                p.setInt(2, bookingID);
                int result = p.executeUpdate();

                if(result > 0) {
                    System.out.println("Successfully cancel booking with ID " + bookingID);
                } else {
                    System.out.println("Unable to cancel this booking");

                }
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot delete a booking that doesn't belongs to this user.");
        }
    }
}
