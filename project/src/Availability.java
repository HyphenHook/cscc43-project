import java.sql.*;
public class Availability {
  Date date;
  int listingID;
  double price;
  String status;
  public Availability (Date date, int listingID, double price, String status)
  {
    this.date = date;
    this.listingID = listingID;
    this.price = price;
    this.status = status;
  }
  public String getStatus()
  {
    return this.status;
  }
  public Date getDate(){
    return this.date;
  }
  public double getPrice(){
    return this.price;
  }
}
