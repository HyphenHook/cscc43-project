import java.io.PrintStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
public class Filter {
  public float distance;
  public float longitude;
  public float latitude;
  public boolean coords, addr, postal;

  public String address;

  public double low, high;
  public boolean price;

  public boolean dates;
  public Date startdate;
  public Date enddate;

  public String postalcode;
  public ArrayList<String> amenities;

  public String location;
  public String priceRange;
  public String dateRange;
  public int ranking;
  public Scanner s;
  public PrintStream p;

  public Filter (Scanner s, PrintStream p)
  {
    this.coords = this.addr = this.postal = this.price = this.dates = false;
    this.low = 0;
    this.high = Double.MAX_VALUE;
    this.location = "";
    this.dateRange = "";
    this.priceRange = "";
    amenities = new ArrayList<>();
    this.s = s;
    this.p = p;
  }
  public void pickLocations()
  {
    this.coords = this.addr = this.postal = false;
    this.location = "";
    int input = 0;
    while (true)
    {
      boolean leave = true;
      p.println ("===========================");
      p.println ("=       Pick Location     =");
      p.println ("===========================");
      p.println ("0 - Pick by Coordinate-Distance");
      p.println ("1 - Pick by Address");
      p.println ("2 - Pick by Similar/Adjacent Postal Code");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          getCoords();
          break;
        }
        case 1:
        {
          getAddress();
          break;
        }
        case 2:
        {
          getPostal();
          break;
        }
        default:
        {
          leave = false;
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) {
        Main.clearScreen();
        break;
      }
    }
  }
  public void getCoords ()
  {
    p.println ("===========================");
    p.println ("Please enter the longitude:");
    longitude = s.nextFloat();
    p.println ("Please enter the latitude:");
    latitude = s.nextFloat();
    p.println("Please enter the distance (km) (less than 0 defaults it to 25 km):");
    distance = s.nextFloat();
    if (distance < 0)
      distance = 25;
    coords = true;
    location = "Long: " + longitude + " | Lat: " + latitude + " | Dist: " + distance + " km";
  }
  public void getAddress()
  {
    p.println ("===========================");
    p.println("Please enter the exact address:");
    s.nextLine();
    address = s.nextLine();
    while (true)
    {
      if (!address.isEmpty())
        break;
      p.println("Please enter the exact non-empty address:");
      address = s.nextLine();
    }
    addr = true;
    location = "Address: " + address;
  }
  public void getPostal()
  {
    p.println ("===========================");
    p.println("Please enter the postal code:");
    s.nextLine();
    postalcode = s.nextLine();
    while (true)
    {
      if (postalcode.length() > 3)
        break;
      p.println("Please enter the postal code:");
      postalcode = s.nextLine();
    }
    postal = true;
    location = "Postal: " + postalcode;
  }
  public void getDates()
  {
    try {
      p.println ("===========================");
      System.out.println ("Enter start date (dd/MM/yyyy):");
      s.nextLine();
      String d1 = s.nextLine();
      System.out.println ("Enter end date (dd/MM/yyyy):");
      String d2 = s.nextLine();
      java.util.Date obj1 = new SimpleDateFormat ("dd/MM/yyyy").parse(d1);
      java.util.Date obj2 = new SimpleDateFormat ("dd/MM/yyyy").parse(d2);
      startdate = new Date (obj1.getTime());
      enddate = new Date (obj2.getTime());
      dateRange = d1 + " - " + d2;
      dates = true;
      Main.clearScreen();
    } catch (ParseException e) {
      System.out.println ("Invalid date!");
      Main.clearScreen();
      return;
    }
  }
  public void getAmenities ()
  {
    amenities = new ArrayList<>();
    p.println ("===========================");
    p.println ("Type the amenities separated by commas!");
    s.nextLine();
    String amen = s.nextLine();
    if (amen.isEmpty())
      return;
    String[] wants = amen.split(",");
    for (String i:wants) amenities.add (i.trim());
    Main.clearScreen();
  }
  public void getPrice(){
    p.println ("===========================");
    p.println ("Low price:");
    low = s.nextDouble();
    p.println ("High price:");
    high = s.nextDouble();
    priceRange = "$" + low + " - " + "$" + high;
    price = true;
  }
}
