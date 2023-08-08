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
          list.add (new Book (r.getInt("bookingID"), r.getInt("listingID"), r.getDate("startdate"), r.getDate("enddate"), r.getString("status"), r.getDate("date"), r.getString("card"), r.getDouble("total"), r.getString("address")));
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
}