import java.sql.*;
import java.util.ArrayList;
public class QueryListing {
  public int listingID;
  public float distance;
  public String address;
  public String status;
  public String type;
  public double total;
  public Date startdate;
  public Date enddate;
  public ArrayList<String> amenities;
  public QueryListing (int listingID, float distance, String address, String status, String type, double total, Date startdate, Date enddate, String postal, String city, String country)
  {
    this.listingID = listingID;
    this.distance = distance;
    this.address = address + ", " + city + " " + postal + ", " + country;
    this.status = status;
    this.type = type;
    this.total = total;
    this.startdate = startdate;
    this.enddate = enddate;
  }

}
