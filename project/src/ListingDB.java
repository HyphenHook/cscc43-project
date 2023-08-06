import java.sql.*;
public class ListingDB {
  public static Listing addListing (String address, String type)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Listing(type, address, status) VALUES (?, ?, 'Active')";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, type);
        s.setString(2, address);
        Listing list = null;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            list = new Listing(r.getInt("listingID"), type, address, "Active");
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
}
