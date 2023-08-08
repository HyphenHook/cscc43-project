import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class Report {
    public static void reportTotalBooking(LocalDate startDate, LocalDate endDate, String city, boolean inCity) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sqlQuery = "SELECT city, COUNT(bookingID) as count FROM LocationInfo NATURAL JOIN Listing NATURAL JOIN Books "
                    + "WHERE status = 'Booked' AND startdate >= ? AND startdate <= ? GROUP BY city";
                String sql2 = "SELECT postalcode, COUNT(bookingID) as count FROM LocationInfo NATURAL JOIN Listing NATURAL JOIN Books "
                    + "WHERE city = ? AND status = 'Booked' AND startdate >= ? AND startdate <= ? GROUP BY postalcode";


                if(!inCity) {
                    PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
                    preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
                    preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String outputCity = resultSet.getString("city");
                        int count = resultSet.getInt("count");

                        System.out.println("City: " + outputCity + ", Total number of Bookings: " + count);
                    }
                    resultSet.close();
                    preparedStatement.close();
                } else {
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    p2.setString(1, city);
                    p2.setDate(2, java.sql.Date.valueOf(startDate));
                    p2.setDate(3, java.sql.Date.valueOf(endDate));
                    ResultSet r2 = p2.executeQuery();
                    System.out.println("Total number of listings in city " + city + " by postal code: ");
                    while (r2.next()) {
                        String postal = r2.getString("postalcode");
                        int c2 = r2.getInt("count");

                        System.out.println("Postal Code : " + postal + ", Total number of Bookings: " + c2);
                    }
                    r2.close();
                    p2.close();

                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportTotalListing(int mode) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT country, COUNT(listingID) as count FROM Listing NATURAL JOIN LocationInfo "
                    + "GROUP BY country";
                String sql2 = "SELECT country, city, COUNT(listingID) as count FROM Listing NATURAL JOIN LocationInfo "
                    + "GROUP BY country, city";
                String sql3 = "SELECT country, city, postalcode, COUNT(listingID) as count FROM Listing NATURAL JOIN LocationInfo "
                    + "GROUP BY country, city, postalcode";


                //Mode 1: count total number of listing per country
                if(mode == 1) {
                    PreparedStatement p1 = con.prepareStatement(sql1);
                    ResultSet r1 = p1.executeQuery();
                    while (r1.next()) {
                        String country = r1.getString("country");
                        int count = r1.getInt("count");

                        System.out.println("Country: " + country + ", Total number of Listings: " + count);
                    }
                    r1.close();
                    p1.close();
                }
                //Mode 2: count total number of listing per country and city
                else if (mode == 2){
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    ResultSet r2 = p2.executeQuery();
                    while (r2.next()) {
                        String country = r2.getString("country");
                        String city = r2.getString("city");
                        int count = r2.getInt("count");

                        System.out.println("Country : " + country + ", City: " + city +
                            ", Total number of Listings: " + count);
                    }
                    r2.close();
                    p2.close();

                }
                //Mode 3: count total number of listing per country, city and postal code
                else {
                    PreparedStatement p3 = con.prepareStatement(sql3);
                    ResultSet r3 = p3.executeQuery();
                    while (r3.next()) {
                        String country = r3.getString("country");
                        String city = r3.getString("city");
                        String postal = r3.getString("postalcode");
                        int count = r3.getInt("count");

                        System.out.println("Country : " + country + ", City: " + city +
                            ", Postal Code: " + postal + ", Total number of Listings: " + count);
                    }
                    r3.close();
                    p3.close();

                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportIndividualHostListingRank(boolean incity, String country, String city) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT userID, COUNT(listingID) as count FROM TheListings NATURAL JOIN Listing "
                    + " NATURAL JOIN LocationInfo WHERE country = ? GROUP BY userID ORDER BY count DESC";
            String sql2 = "SELECT userID, COUNT(listingID) as count FROM TheListings NATURAL JOIN Listing "
                    + " NATURAL JOIN LocationInfo WHERE country = ? AND city = ? GROUP BY userID ORDER BY count DESC";


            //Mode 1: Rank number of listings they have per country
            if(!incity) {
                PreparedStatement p1 = con.prepareStatement(sql1);
                p1.setString(1, country);
                ResultSet r1 = p1.executeQuery();
                System.out.println("Country: " + country);
                while (r1.next()) {
                    int userID = r1.getInt("userID");
                    int count = r1.getInt("count");

                    System.out.println("User: " + userID + ", Total number of Listings: " + count);
                }
                System.out.println("                                                                   ");
                r1.close();
                p1.close();
            }
            //Mode 2: Rank number of listings they have per country and city
            else {
                PreparedStatement p2 = con.prepareStatement(sql2);
                p2.setString(1, country);
                p2.setString(2, city);
                ResultSet r2 = p2.executeQuery();
                System.out.println("Country: " + country + ", City: " + city);
                while (r2.next()) {
                    int userID = r2.getInt("userID");
                    int count = r2.getInt("count");

                    System.out.println("User: " + userID + ", Total number of Listings: " + count);
                }
                System.out.println("                                                                   ");
                r2.close();
                p2.close();

            }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportTotalHostListingRank(boolean incity) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT DISTINCT country FROM LocationInfo";
                String sql2 = "SELECT DISTINCT country, city FROM LocationInfo";


                //Mode 1: Rank number of listings they have per country
                if(!incity) {
                    PreparedStatement p1 = con.prepareStatement(sql1);
                    ResultSet r1 = p1.executeQuery();
                    while (r1.next()) {
                        String country = r1.getString("country");
                        reportIndividualHostListingRank(incity, country, "");
                    }
                    r1.close();
                    p1.close();
                }
                //Mode 2: Rank number of listings they have per country and city
                else {
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    ResultSet r2 = p2.executeQuery();
                    while (r2.next()) {
                        String country = r2.getString("country");
                        String city = r2.getString("city");
                        reportIndividualHostListingRank(incity, country, city);
                    }
                    r2.close();
                    p2.close();

                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportLikelyCommercialHost(boolean incity) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT country, city, userID, number " +
                              "FROM ( " +
                              "    SELECT country, city, userID, COUNT(listingID) AS number, " +
                              "           SUM(COUNT(listingID)) OVER (PARTITION BY country, city) AS total " +
                              "    FROM TheListings Natural Join Listing Natural JOIN LocationInfo " +
                              "    GROUP BY country, city, userID " +
                              ") AS subquery " +
                              "WHERE number > 0.1 * total";

                String sql2 = "SELECT country, userID, number " +
                    "FROM ( " +
                    "    SELECT country, userID, COUNT(listingID) AS number, " +
                    "           SUM(COUNT(listingID)) OVER (PARTITION BY country) AS total " +
                    "    FROM TheListings Natural Join Listing Natural JOIN LocationInfo " +
                    "    GROUP BY country, userID " +
                    ") AS subquery " +
                    "WHERE number > 0.1 * total";



                //Mode 1: Search for such users per city
                if(incity) {
                    PreparedStatement p1 = con.prepareStatement(sql1);
                    ResultSet r1 = p1.executeQuery();

                    while (r1.next()) {
                        String country = r1.getString("country");
                        String city = r1.getString("city");
                        int userID = r1.getInt("userID");
                        int num = r1.getInt("number");
                        System.out.println("Country: " + country + ", City: " + city + ", userID: " + userID
                            + " , number: " + num);
                    }
                    r1.close();
                    p1.close();
                }
                //Mode 2: Search for such user per country
                else {
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    ResultSet r2 = p2.executeQuery();
                    while (r2.next()) {
                        String country = r2.getString("country");
                        int userID = r2.getInt("userID");
                        int num = r2.getInt("number");
                        System.out.println("Country: " + country + " , userID: " + userID
                            + " , number: " + num);
                    }
                    r2.close();
                    p2.close();

                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static int getTotalBookings(int userID, java.sql.Date start) {
        int total = 0;
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sqlQuery = "SELECT COUNT(*) AS count FROM TheBookings NATURAL JOIN Books WHERE userID = ? AND YEAR(startdate) = ?";

                int year = start.toLocalDate().getYear();

                PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, year);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    total = resultSet.getInt("count");
                }

                resultSet.close();
                preparedStatement.close();
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          return total;
    }

    public static void reportCityRenterRank(java.sql.Date start, java.sql.Date end, String country, String city) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT userID, COUNT(*) as count " +
                    "FROM TheBookings NATURAL JOIN Books NATURAL JOIN Listing NATURAL JOIN LocationInfo "
                    + "WHERE country = ? AND city = ? AND startdate >= ? AND startdate <= ? " +
                    "GROUP BY userID ORDER BY count DESC";


                PreparedStatement p1 = con.prepareStatement(sql1);
                p1.setString(1, country);
                p1.setString(2, city);
                p1.setDate(3, start);
                p1.setDate(4, end);
                ResultSet r1 = p1.executeQuery();
                if(r1.next()){
                    System.out.println("Country: " + country + " , City: " + city);
                }
                while (r1.next()) {
                    int userID = r1.getInt("userID");
                    int num = r1.getInt("count");
                    if(getTotalBookings(userID, start) >= 2) {
                        System.out.println("User ID: " + userID + ", Total number of Bookings: " + num);
                    }
                }
                r1.close();
                p1.close();
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportRenterRank(LocalDate startDate, LocalDate endDate, boolean incity) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT userID, COUNT(*) as count " +
                          "FROM TheBookings NATURAL JOIN Books " +
                          "WHERE startdate >= ? AND startdate <= ? " +
                          "GROUP BY userID " +
                          "ORDER BY count DESC";

                String sql2 = "SELECT DISTINCT country, city FROM LocationInfo";


                //Mode 1: Rank user for all their bookings in given time period
                if(!incity) {
                    PreparedStatement p1 = con.prepareStatement(sql1);
                    p1.setDate(1, java.sql.Date.valueOf(startDate));
                    p1.setDate(2, java.sql.Date.valueOf(endDate));
                    ResultSet r1 = p1.executeQuery();

                    while (r1.next()) {
                        int userID = r1.getInt("userID");
                        int num = r1.getInt("count");
                        System.out.println("User ID: " + userID + ", Total number of Bookings: " + num);
                    }
                    r1.close();
                    p1.close();
                }
                //Mode 2: Search for such user per city
                else {
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    ResultSet r2 = p2.executeQuery();
                    while (r2.next()) {
                        String country = r2.getString("country");
                        String city = r2.getString("city");
                        reportCityRenterRank(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate), country, city);
                    }
                    r2.close();
                    p2.close();

                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportAllCancel(int year, int max, boolean isHost) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT userID, COUNT(*) AS count " +
                    "FROM Books NATURAL JOIN TheListings " +
                    "WHERE status = 'Canceled' AND YEAR(startdate) = ? " +
                    "GROUP BY userID " +
                    "HAVING count = ?";

                String sql2 = "SELECT userID, COUNT(*) AS count " +
                    "FROM TheBookings NATURAL JOIN Books " +
                    "WHERE status = 'Canceled' AND YEAR(startdate) = ? " +
                    "GROUP BY userID " +
                    "HAVING count = ?";

                if(isHost) {
                    PreparedStatement ps1 = con.prepareStatement(sql1);
                    ps1.setInt(1, year);
                    ps1.setInt(2, max);
                    ResultSet r1 = ps1.executeQuery();

                    System.out.println("Hosts with the largest number of cancellations in year " + year + ":");
                    while (r1.next()) {
                        int hostID = r1.getInt("userID");
                        int numHost = r1.getInt("count");
                        System.out.println("Host ID: " + hostID + ", Number of Cancellations: " + numHost);
                    }

                    r1.close();
                    ps1.close();

                } else {

                    PreparedStatement ps2 = con.prepareStatement(sql2);
                    ps2.setInt(1, year);
                    ps2.setInt(2, max);
                    ResultSet r2 = ps2.executeQuery();

                    System.out.println("Renters with the largest number of cancellations in year " + year + ":");
                    while (r2.next()) {
                        int renterID = r2.getInt("userID");
                        int numRenter = r2.getInt("count");
                        System.out.println("Renter ID: " + renterID + ", Number of Cancellations: " + numRenter);
                    }

                    r2.close();
                    ps2.close();
                }
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }

    public static void reportMaxCancel(int year) {
        try{
            Connection con = Connector.getConnection();
            if (con != null)
            {
                String sql1 = "SELECT userID, COUNT(*) AS count " +
                          "FROM Books NATURAL JOIN TheListings " +
                          "WHERE status = 'Canceled' AND YEAR(startdate) = ? " +
                          "GROUP BY userID " +
                          "ORDER BY count DESC " +
                          "LIMIT 1";

                String sql2 = "SELECT userID, COUNT(*) AS count " +
                          "FROM TheBookings NATURAL JOIN Books " +
                          "WHERE status = 'Canceled' AND YEAR(startdate) = ? " +
                          "GROUP BY userID " +
                          "ORDER BY count DESC " +
                          "LIMIT 1";

                PreparedStatement ps1 = con.prepareStatement(sql1);
                ps1.setInt(1, year);
                ResultSet r1 = ps1.executeQuery();

                if (r1.next()) {
                    int max = r1.getInt("count");
                    reportAllCancel(year, max, true);
                }

                r1.close();
                ps1.close();

                System.out.println("                           ");

                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, year);
                ResultSet r2 = ps2.executeQuery();

                if (r2.next()) {
                    int max2 = r2.getInt("count");
                    reportAllCancel(year, max2, false);
                }

                r2.close();
                ps2.close();
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
    }
    
  
}
