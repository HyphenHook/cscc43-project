import java.sql.*;
public class Book {
  public int bookingID;
  public int listingID;
  public Date startdate;
  public Date enddate;
  public Date date;
  public String status;
  public String card;
  public double total;
  public String address;
  public Book (int bookingID, int listingID, Date startdate, Date enddate, String status, Date date, String card, double total, String address)
  {
    this.address = address;
    this.bookingID = bookingID;
    this.listingID = listingID;
    this.startdate = startdate;
    this.enddate = enddate;
    this.status = status;
    this.date = date;
    this.card = card;
    this.total = total;
  }

}
