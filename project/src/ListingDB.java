import java.sql.*;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
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

}
