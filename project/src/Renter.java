import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Renter {
  static PrintStream p = System.out;
  static Scanner s = new Scanner(System.in);
  public static void main(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      printRenterMain();
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          showBookings();
          break;
        }
        case 1:
        {
          searchListings();
          break;
        }
        case 2:
        {
          showPayments();
          break;
        }
        case 3:
        {
          Main.clearScreen();
          leave = true;
          break;
        }
        default:
        {
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) break;
    }
  }
  public static void printRenterMain(){
    p.println ("===========================");
    p.println ("=      Renter Service     =");
    p.println ("===========================");
    p.println ("0 - Show My Bookings");
    p.println ("1 - Search Listings to Book");
    p.println ("2 - Show Payment Methods");
    p.println ("3 - Back");
    p.println ("===========================");
    p.println ("Please select:");
  }
  public static void searchListings(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    Filter f = new Filter(s, p);
    while (true)
    {
      Main.clearScreen();
      p.println ("===========================");
      p.println ("=      Search Listing     =");
      p.println ("===========================");
      p.println ("Target Location: " + f.location);
      p.println ("Target Date: " + f.dateRange);
      p.println ("Target Amenities: " + f.amenities.toString());
      p.println ("Target Price Range: " + f.priceRange);
      p.println ("===========================");
      p.println ("0 - Pick Location (coordinate, postal, or address)");
      p.println ("1 - Pick Date Range");
      p.println ("2 - Pick Amenities");
      p.println ("3 - Pick Price Range");
      p.println ("4 - Clear Filter");
      p.println ("5 - Search");
      p.println ("6 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          f.pickLocations();
          break;
        }
        case 1:
        {
          f.getDates();
          break;
        }
        case 2:
        {
          f.getAmenities();
          break;
        }
        case 3:
        {
          f.getPrice();
          break;
        }
        case 4:
        {
          f = new Filter(s, p);
          break;
        }
        case 5:
        {
          requestSearch(f);
          break;
        }
        case 6:
        {
          leave = true;
          break;
        }
        default:
        {
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) 
      {
        Main.clearScreen();
        break;
      }
    }
  }
  public static void requestSearch (Filter f)
  {
    int input = 0;
    while (true)
    {
      boolean leave = true;
      p.println ("===========================");
      p.println ("=       Pick Ranking      =");
      p.println ("===========================");
      p.println ("0 - Ascending Cost");
      p.println ("1 - Descending Cost");
      if (f.coords)
        p.println ("2 - Closest Distance");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      if (input == 0) f.ranking = 0;
      else if (input == 1) f.ranking = 1;
      else if (f.coords && input == 2) f.ranking = 2;
      else{
        leave = false;
        Main.clearScreen();
        p.println ("Please select a valid option!");
      }
      if (leave) break;
    }
    displaySearch(f);
  }
  public static void displayDetails ()
  {
    p.println("Choose the index of the listing!");
    int input = s.nextInt();
    p.println ("====================================================");
    Query.showDetailQuery(input);
  }
  public static void quickBook(){
    p.println("Choose the index of the listing!");
    int input = s.nextInt();
    p.println ("====================================================");
    QueryListing i = Query.getQueryList(input);
    p.println("Choose payment!");
    String card = pickPayment();
    if (card == null)
    {
      p.println("Something gone wrong!");
      return;
    }
    else
    {
      BookingDB.bookAvailability(i.listingID, i.startdate, i.enddate);
      int bID = BookingDB.makeBooking(i.listingID, i.startdate, i.enddate, card, i.total);
      BookingDB.linkBookingUser(bID);
      p.println ("Booking succeeded!");
    }
  }
  public static String pickPayment(){
    int input = 0;
    boolean leave = false;
    String card = null;
    while (true)
    {
      PaymentDB.fetchPaymentMethods ();
      p.println ("===========================");
      p.println ("=     Payment Methods     =");
      p.println ("===========================");
      PaymentDB.showPaymentMethods();
      p.println ("===========================");
      p.println ("0 - Add Credit/Debit Card");
      p.println ("1 - Remove Credit/Debit Card");
      p.println ("2 - Use Credit/Debit Card");
      p.println ("3 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          if (!addCreditCard())
            p.println ("Failed to add card!");
          else
            p.println ("Card added successfully!");
          break;
        }
        case 1:
        {
          if (!removeCreditCard())
            p.println ("Failed to remove card!");
          else
            p.println ("Card removed successfully!");
          break;
        }
        case 2:
        {
          card = useCreditCard();
          if (card != null) leave = true;
          break;
        }
        case 3:
        {
          Main.clearScreen();
          leave = true;
          break;
        }
        default:
        {
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) break;
    }
    return card;
  }
  public static String useCreditCard(){
    p.println ("Please specify the index of the card you wish to use:");
    int index = s.nextInt();
    Main.clearScreen();
    return PaymentDB.useCreditCard(index);
  }
  public static void displaySearch (Filter f)
  {
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      Query.fetchQuery(f);
      p.println ("====================================================");
      p.println ("=                   Search Results                 =");
      p.println ("====================================================");
      Query.showQuery();
      p.println ("====================================================");
      p.println ("0 - See Listing Details");
      p.println ("1 - Quick book");
      p.println ("2 - Back");
      p.println ("====================================================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          displayDetails();
          break;
        }
        case 1:
        {
          quickBook();
          leave = true;
          break;
        }
        case 2:
        {
          Main.clearScreen();
          leave = true;
          break;
        }
        default:
        {
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) break;
    }
  }
  public static void showBookings(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      BookingDB.fetchBookings();
      p.println ("===========================");
      p.println ("=       All Bookings      =");
      p.println ("===========================");
      BookingDB.showBookings();
      p.println ("===========================");
      p.println ("0 - Cancel a booking");
      p.println ("1 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          if (!cancelBooking())
            p.println ("Failed to cancel a booking!");
          else
            p.println ("Successfully canceled the booking!");
          break;
        }
        case 1:
        {
          Main.clearScreen();
          leave = true;
          break;
        }
        default:
        {
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) break;
    }
  }
  public static boolean cancelBooking()
  {
    p.println ("Please specify the index of the booking you wish to cancel:");
    int index = s.nextInt();
    Main.clearScreen();
    return BookingDB.cancelBooking(index);
  }
  public static void showPayments(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      PaymentDB.fetchPaymentMethods ();
      p.println ("===========================");
      p.println ("=     Payment Methods     =");
      p.println ("===========================");
      PaymentDB.showPaymentMethods();
      p.println ("===========================");
      p.println ("0 - Add Credit/Debit Card");
      p.println ("1 - Remove Credit/Debit Card");
      p.println ("2 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          if (!addCreditCard())
            p.println ("Failed to add card!");
          else
            p.println ("Card added successfully!");
          break;
        }
        case 1:
        {
          if (!removeCreditCard())
            p.println ("Failed to remove card!");
          else
            p.println ("Card removed successfully!");
          break;
        }
        case 2:
        {
          Main.clearScreen();
          leave = true;
          break;
        }
        default:
        {
          Main.clearScreen();
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (leave) break;
    }
  }
  public static boolean addCreditCard()
  {
    s.nextLine();
    p.println("Please enter the 16 digit of the card:");
    String card = s.nextLine();
    while (true)
    {
      if (card.length() < 16 || card.length() > 16)
        p.println ("Invalid credit card! Please try again!");
      else if (PaymentDB.isDupedCard(card))
        p.println ("Card is already on your account!");
      else break;
      p.println("Please enter the 16 digit of the card:");
      card = s.nextLine();
    }
    Date expiry = getExpireDate();
    while (true)
    {
      if (expiry != null && PaymentDB.isExpired(expiry))
      {
        p.println ("Card has expired! Please enter a new one!");
        return false;
      }
      else if (expiry != null) break;
      expiry = getExpireDate();
    }
    p.println("Enter the card holder's name:");
    String name = s.nextLine();
    while (true)
    {
      if (card.isEmpty())
        p.println ("Name cannot be empty!");
      else break;
      p.println("Enter the card holder's name:");
      name = s.nextLine();
    }
    int result = PaymentDB.addCreditCard(card, expiry, name);
    Main.clearScreen();
    return PaymentDB.linkCardWithUser (result);
  }
  public static boolean removeCreditCard()
  {
    p.println ("Please specify the index of the card you wish to delete:");
    int index = s.nextInt();
    Main.clearScreen();
    return PaymentDB.deleteCreditCard(index);
  }
  public static Date getExpireDate()
  {
    try{
      p.println ("Please enter the expiry date in format MM/YYYY:");
      String date = s.nextLine().trim();
      java.util.Date obj = new SimpleDateFormat("MM/yyyy").parse(date);
      return new Date(obj.getTime());
    }
    catch (ParseException e) {
      p.println ("Invalid date!");
      return null;
    }
  }
}
