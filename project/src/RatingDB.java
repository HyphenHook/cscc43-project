import java.util.ArrayList;
import java.sql.*;

public class RatingDB {
  static ArrayList<Rating> list;

  public static Rating createComments(int rating, String text){
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Rating(rating, comment) VALUES (?, ?)";
        PreparedStatement s = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        s.setInt(1, rating);
        s.setString(2, text);
        Rating list = null;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            list = new Rating(r.getInt(1), text, rating);
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

  public static int gethost(int listingID){
    int answer = 0;
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM TheListings WHERE listingID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);
        ResultSet r = s.executeQuery();

        if (r.next())
        {
          answer = r.getInt("userID");
        }
        s.close();
        con.close();
        return answer;
      }
      return answer;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return answer;
    }
  }

  public static boolean checkComplete(int bookingID){
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String q1 = "SELECT * FROM Books WHERE bookingID = ? and status = 'Complete'";

        PreparedStatement s1 = con.prepareStatement(q1);
        s1.setInt(1, bookingID);
   
        ResultSet r = s1.executeQuery();
        boolean result = false;

        if(r.next()){
          result = true;
        }
        
        s1.close();
        con.close();
        return result;
       
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean addRates(int ratingID, int listingID, int toID, boolean isRenter){
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String q1 = "INSERT INTO RenterRates(renterID, ratingID, hostID, listingID) VALUES (?, ?, ?, ?)";
        String q2 = "INSERT INTO HostRates(hostID, ratingID, renterID) VALUES (?, ?, ?)";
        if (isRenter) {
          PreparedStatement s1 = con.prepareStatement(q1);
          s1.setInt(1, User.getInstance().getID());
          s1.setInt(2, ratingID);
          s1.setInt(3, toID);
          s1.setInt(4, listingID);

          boolean success1 = s1.executeUpdate() >= 1;
          s1.close();
          con.close();
          return success1;
        } else {
          PreparedStatement s2 = con.prepareStatement(q2);
          s2.setInt(1, User.getInstance().getID());
          s2.setInt(2, ratingID);
          s2.setInt(3, toID);

          boolean success2 = s2.executeUpdate() >= 1;
          s2.close();
          con.close();
          return success2;
        }
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public static void fetchRatingListing(int listingID) {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM RenterRates NATURAL JOIN Rating WHERE listingID = ? AND hostID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, listingID);
        s.setInt(2, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Rating(r.getInt("ratingID"), r.getString("comment"), r.getInt("rating")));
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

  public static void fetchRatingRenter(){
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM HostRates NATURAL JOIN Rating WHERE renterID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Rating(r.getInt("ratingID"), r.getString("comment"), r.getInt("rating")));
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

  public static void showRatings ()
  {
    if (list.isEmpty())
    {
      System.out.println ("You have not been rated.");
      return;
    }
    System.out.println("========            Ratings           ========");
    for (Rating i: list)
    {
      System.out.println("RatingID: " + i.ratingID);
      System.out.println("Rating: " + i.rating);
      System.out.println("Comment: " + i.comment);
      System.out.println("==============================");
    }
  }
  
}
