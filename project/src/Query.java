import java.util.ArrayList;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Query {
  private static ArrayList<QueryListing> list;
  public static void fetchQuery (Filter f)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query;
        PreparedStatement s;
        query = "DROP VIEW IF EXISTS t1, t2, t3";
        s = con.prepareStatement(query);
        s.executeUpdate();
        s.close();
        if (!f.addr && !f.coords && !f.postal)
        {
          query = "CREATE VIEW t1 AS SELECT Listing.*, LocationInfo.postalcode, LocationInfo.city, LocationInfo.country FROM Listing INNER JOIN LocationInfo ON Listing.address = LocationInfo.address WHERE status = 'Active'";
          s = con.prepareStatement(query);
          s.executeUpdate();
          s.close();
        }
        else if (f.addr)
        {
          query = "CREATE VIEW t1 AS SELECT Listing.*, LocationInfo.postalcode, LocationInfo.city, LocationInfo.country FROM Listing INNER JOIN LocationInfo ON Listing.address = LocationInfo.address WHERE status = 'Active' AND LocationInfo.address = ?";
          s = con.prepareStatement(query);
          s.setString (1, f.address);
          s.executeUpdate();
          s.close();
        }
        else if(f.postal)
        {
          query = "CREATE VIEW t1 AS SELECT Listing.*, LocationInfo.postalcode, LocationInfo.city, LocationInfo.country FROM Listing INNER JOIN LocationInfo ON Listing.address = LocationInfo.address WHERE status = 'Active' AND (postalcode = ? OR UPPER(LEFT(postalcode, 3)) = UPPER(LEFT(?, 3)))";
          s = con.prepareStatement(query);
          s.setString (1, f.postalcode);
          s.setString(2, f.postalcode);
          s.executeUpdate();
          s.close();
        }
        else if (f.coords)
        {
          query = "CREATE VIEW t1 AS SELECT Listing.*, LocationInfo.postalcode, LocationInfo.city, LocationInfo.country, ST_Distance_Sphere(POINT(?, ?), POINT(LocationInfo.longitude, LocationInfo.latitude)) / 1000 AS dist FROM Listing INNER JOIN LocationInfo ON Listing.address = LocationInfo.address WHERE status = 'Active' HAVING dist <= ?";
          s = con.prepareStatement(query);
          s.setFloat (1, f.longitude);
          s.setFloat (2, f.latitude);
          s.setFloat (3, f.distance);
          s.executeUpdate();
          s.close();
        }
        if (!f.dates)
        {
          query = "CREATE VIEW t2 AS SELECT listingID, MIN(price) AS minPrice, MIN(date) AS minDate FROM Availability WHERE status = 'Available' GROUP BY listingID HAVING minPrice BETWEEN ? AND ?;";
          s = con.prepareStatement(query);
          s.setDouble(1, f.low);
          s.setDouble(2, f.high);
          s.executeUpdate();
          s.close();
        }
        else
        {
          query = "CREATE VIEW t2 AS SELECT listingID, SUM(price) AS minPrice FROM Availability WHERE status = 'Available' AND date BETWEEN ? AND ? GROUP BY listingID HAVING COUNT(*) = DATEDIFF(?, ?) + 1 AND minPrice BETWEEN ? AND ?";
          s = con.prepareStatement(query);
          s.setDate(1, f.startdate);
          s.setDate(2, f.enddate);
          s.setDate(3, f.enddate);
          s.setDate(4, f.startdate);
          s.setDouble(5, f.low);
          s.setDouble(6, f.high);
          s.executeUpdate();
          s.close();
        }
        if (!f.amenities.isEmpty())
        {
          query = "CREATE VIEW t3 AS SELECT listingID FROM Amenities WHERE name IN (";
          for (int i = 0; i < f.amenities.size(); i++) {
              query += "'" + f.amenities.get(i) + "'";
              if (i != f.amenities.size() - 1) {
                  query += ", ";
              }
          }
          query += ") GROUP BY listingID HAVING COUNT(DISTINCT name) = ?";
          s = con.prepareStatement(query);
          s.setInt(1, f.amenities.size());
          s.executeUpdate();
          s.close();
        }
        query = "SELECT * FROM t1 INNER JOIN t2 ON t1.listingID = t2.listingID";
        if (!f.amenities.isEmpty()) query += " INNER JOIN t3 ON t3.listingID = t2.listingID";
        if (f.ranking == 0) query += " ORDER BY t2.minPrice ASC";
        else if (f.ranking == 1) query += " ORDER BY t2.minPrice DESC";
        else query += " ORDER BY t1.dist ASC";
        s = con.prepareStatement(query);
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        int i = 0;
        while (r.next())
        {
          float distance = -1;
          if (f.coords)
            distance = r.getFloat("dist");
          if (f.dates)
            list.add (new QueryListing(r.getInt("listingID"), distance, r.getString("address"), r.getString("status"), r.getString("type"), r.getDouble("minPrice"), f.startdate, f.enddate, r.getString("postalcode"), r.getString("city"), r.getString("country")));
          else
            list.add (new QueryListing(r.getInt("listingID"), distance, r.getString("address"), r.getString("status"), r.getString("type"), r.getDouble("minPrice"), r.getDate("minDate"), r.getDate("minDate"), r.getString("postalcode"), r.getString("city"), r.getString("country")));
          loadAmenities (list.get(i++));
        }
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
  public static void loadAmenities (QueryListing q)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT name FROM Amenities WHERE listingID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, q.listingID);
        q.amenities = new ArrayList<>();
        ResultSet r = s.executeQuery();
        while (r.next())
          q.amenities.add (r.getString("name"));
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
  public static void showQuery()
  {
    if (list.isEmpty())
    {
      System.out.println ("No result!");
      return;
    }
    int j = 0;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    for (QueryListing i:list)
    {
      System.out.println ("========================= " + j++ + " ========================");
      System.out.println ("Address: " + i.address);
      System.out.println ("Type: " + i.type + " | " + "Status: " + i.status);
      if (i.distance != -1)
        System.out.println (String.format("Distance %.6f km", i.distance));
      System.out.println ("Dates: " + df.format(i.startdate) + " to " + df.format(i.enddate) + " | Costs: $" + i.total);
      System.out.println ("Amenities: " + i.amenities.toString());
    }
  }

  public static boolean seeComments (int index)
  {
    if (index >= list.size() || index < 0)
    {
      System.out.println ("The listing hasn't have comments yet.");
      return false;
    }
    RatingDB.fetchRatingListing(list.get(index).listingID);
    RatingDB.showRatings();
    return true;
  }
}
