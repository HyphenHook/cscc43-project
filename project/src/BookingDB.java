import java.util.ArrayList;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BookingDB {
  private static ArrayList<Book> list;
  public static void fetchBookings ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM (TheBookings INNER JOIN Books ON TheBookings.bookingID = Books.bookingID) INNER JOIN Listing ON Listing.listingID = Books.listingID WHERE userID = ? ORDER BY date DESC";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt (1, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Book (r.getInt("bookingID"), r.getInt("listingID"), r.getDate("startdate"), r.getDate("enddate"), r.getString("status"), r.getDate("date"), r.getString("card"), r.getDouble("total"), 
                                r.getString("address"), true));
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
  public static void showBookings()
  {
    if (list.isEmpty())
    {
      System.out.println ("You have no bookings!");
      return;
    }
    int j = 0;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    for (Book i:list)
    {
      System.out.println ("========================= " + j++ + " ========================");
      System.out.println ("Address: " + i.address);
      System.out.println ("Date Booking Made: " + df.format(i.date));
      System.out.println ("Date Booked: " + df.format(i.startdate) + " - " + df.format(i.enddate));
      System.out.println ("Payment Card: ****" + i.card + " | " + "Total: $" + Math.round(i.total * 100.0) / 100.0);
      System.out.println ("Status: " + i.status);
    }
  }
    //Fetch bookings for host
  public static void fetchHostBookings ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM TheListings NATURAL JOIN Books WHERE userID = ? ORDER BY date DESC";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Book (r.getInt("bookingID"), r.getInt("listingID"), r.getDate("startdate"), 
                                r.getDate("enddate"), r.getString("status"), r.getDate("date"), r.getString("card"), r.getDouble("total"), "", false));
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

  public static void showHostBookings()
  {
    if (list.isEmpty())
    {
      System.out.println ("You listings have no bookings!");
      return;
    }
    int j = 0;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    for (Book i:list)
    {
      System.out.println ("========================= " + j++ + " ========================");
      System.out.println("BookingID: " + i.bookingID);
      System.out.println("Date Made: " + i.date);
      System.out.println ("Price: $" + i.total);
      System.out.println ("Date Booked: " + df.format(i.startdate) + " - " + df.format(i.enddate));
      System.out.println ("Status: " + i.status);
    }
  }
  public static boolean bookAvailability (int listingID, Date start, Date end)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "UPDATE Availability SET status = 'Booked' WHERE listingID = ? AND date BETWEEN ? AND ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt (1, listingID);
        s.setDate (2, start);
        s.setDate (3, end);
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
  public static boolean cancelAvailability (Book i)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "UPDATE Availability SET status = 'Available' WHERE listingID = ? AND date BETWEEN ? AND ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt (1, i.listingID);
        s.setDate (2, i.startdate);
        s.setDate (3, i.enddate);
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
  public static boolean cancelBook (int bookingID)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "UPDATE Books SET status = 'Canceled' WHERE bookingID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, bookingID);
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
  public static boolean cancelBooking (int index)
  {
    if (index >= list.size() || index < 0)
    {
      System.out.println ("You don't have a booking at that position!");
      return false;
    }
    if (list.get(index).status.equals("Canceled"))
    {
      System.out.println ("The booking is already canceled!");
      return false;
    }
    if (list.get(index).status.equals("Completed"))
    {
      System.out.println ("The booking is already completed!");
      return false;
    }
    return (cancelBook (list.get(index).bookingID) && cancelAvailability(list.get(index)));
  }

  public static boolean comments (int index, int rating, String text)
  {
    if (index >= list.size() || index < 0)
    {
      System.out.println ("You don't have a booking at that position!");
      return false;
    }
    if (!(list.get(index).status.equals("Completed")))
    {
      System.out.println ("Cannot comment on it since the booking is not complete.");
      return false;
    }
    boolean isRenter = list.get(index).isRenter;
    int listingID = list.get(index).listingID;
    Rating r = RatingDB.createComments(rating, text);
    if(isRenter){
      return RatingDB.addRates(r.ratingID, listingID, RatingDB.gethost(listingID), isRenter);
    } else {
      return RatingDB.addRates(r.ratingID, listingID, RatingDB.getRenter(listingID), isRenter);
    }
  }

  public static boolean seeComments(int index){
    if (index >= list.size() || index < 0)
    {
      System.out.println ("Invalid bookingID.");
      return false;
    }
    RatingDB.fetchRatingListing(list.get(index).bookingID);
    RatingDB.showRatings();
    return true;
  }
  public static boolean linkBookingUser (int bookingID)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO TheBookings(userID, bookingID) VALUES (?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        s.setInt(2, bookingID);
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
  public static int makeBooking (int listingID, Date start, Date end, String card, double total)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO Books (listingID, startdate, enddate, status, card, date, total) VALUES (?, ?, ?, ?, ?, CURDATE(), ?)";
        PreparedStatement s = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        s.setInt(1, listingID);
        s.setDate(2, start);
        s.setDate(3, end);
        s.setString(4, "Booked");
        s.setString(5, card);
        s.setDouble(6, total);
        int success = -1;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            success = r.getInt(1);
          r.close();
        }
        s.close();
        con.close();
        return success;
      }
      return -1;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return -1;
    }
  }
}
