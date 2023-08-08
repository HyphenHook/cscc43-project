import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class ListingDB {
  private static ArrayList<Listing> list;
  public static Listing addListing (String address, String type)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Listing(type, address, status) VALUES (?, ?, 'Active')";
        PreparedStatement s = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        s.setString(1, type);
        s.setString(2, address);
        Listing list = null;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            list = new Listing(r.getInt(1), type, address, "Active");
          r.close();
        }
        s.close();
        con.close();
        return list;
      }
      return null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  public static boolean addLocation (double latitude, double longitude, String postalcode, String address, String city, String country)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO LocationInfo(latitude, longitude, postalcode, address, city, country) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setDouble(1, latitude);
        s.setDouble(2, longitude);
        s.setString(3, postalcode);
        s.setString(4, address);
        s.setString(5, city);
        s.setString(6, country);
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean isLocationActive (String address)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT listingID FROM Listing WHERE address = ? AND status = 'Active'";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, address);
        ResultSet r = s.executeQuery();
        boolean success = r.next();
        r.close();
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean isLocationInactive (String address)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT listingID FROM Listing WHERE address = ? AND status = 'Inactive'";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, address);
        ResultSet r = s.executeQuery();
        boolean success = r.next();
        r.close();
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean addAmenities (int listingID, String amenity)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Amenities(name, listingID) VALUES (?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);
        s.setString(2, amenity);
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLIntegrityConstraintViolationException e)
    {
      System.out.println ("Already have that amenity for that listing!");
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean addAvailability (int listingID, Date date, double price)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Availability(listingID, date, price, status) VALUES (?, ?, ?, 'Available')";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);
        s.setDate(2, date);
        s.setDouble(3, price);
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLIntegrityConstraintViolationException e)
    {
      System.out.println ("Already have an availability for that date!");
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean linkListingToHost (Listing list)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO TheListings(userID, listingID) VALUES (?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        s.setInt(2, list.getID());
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static void seeAvailability (int listingID){
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM Availability WHERE listingID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);

        ResultSet r = s.executeQuery();
        while (r.next())
          System.out.println(r.getDate("date") + "  |  " + r.getDouble("price") + "  |  " + r.getString("status"));
        r.close();
        s.close();
        con.close();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  public static boolean modifyAvailability (int listingID, Date date, double price)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "UPDATE Availability SET price = ? WHERE listingID = ? AND date = ? AND status = 'Available'";
        PreparedStatement s = con.prepareStatement(query);
        s.setDouble(1, price);
        s.setInt(2, listingID);
        s.setDate(3, date);
        boolean success = s.executeUpdate() >= 1;
        if (!success)
          System.out.println ("The date you are trying to modify is unavailable! Please cancel the booking associated with this date before modifying the price!");
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean removeAvailability (int listingID, Date date)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "DELETE FROM Availability WHERE listingID = ? AND date = ? status = 'Available'";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);
        s.setDate(2, date);
        boolean success = s.executeUpdate() >= 1;
        if (!success)
          System.out.println ("The date you are trying to modify is unavailable! Please cancel the booking associated with this date before removing the availability!");
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

   public static void fetchListings ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM TheListings NATURAL JOIN Listing WHERE userID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Listing(r.getInt("listingID"), r.getString("type"), 
                                  r.getString("address"),r.getString("status")));
        r.close();
        s.close();
        con.close();
      }
    }
    catch (SQLException e)
    {
      return;
    }
  }

  public static void showListings ()
  {
    if (list.isEmpty())
    {
      System.out.println ("You have no listings.");
      return;
    }
    int j = 0;
    for (Listing i: list)
    {
      System.out.println(j++ + ". " + i.getID() + "   |   " + i.getType() + "  |  " + i.getAddress());
    }
  }

  public static boolean checkRelation(int userID, int listingID){
    try {
      Connection con = Connector.getConnection();
      if (con != null)
      {
        boolean result = false;
        String sqlQuery = "SELECT * FROM TheListings WHERE userID = ? AND listingID = ?";
        PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, listingID);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            result = true;
        } 

        preparedStatement.close();
        return result;
      }
      return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
  }

  public static boolean checkAvailability(int listingID, LocalDate startDate, LocalDate endDate, boolean isBooked){
    try {
      Connection con = Connector.getConnection();
      if (con != null)
      {
        boolean result = false;
        String sqlQuery = "SELECT COUNT(*) AS count FROM Availability Where listingID = ? AND status = 'Available' AND date BETWEEN ? AND ?";
        PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);

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
          if(isBooked){
            if (count == total) {
              result = true;
            }
          } else {
            if (count == 0){
              result = true;
            }
          }
        } 

        preparedStatement.close();
        return result;
      }
      return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
  }


}
