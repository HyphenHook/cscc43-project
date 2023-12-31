import java.io.PrintStream;
import java.util.Scanner;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class Host {
  static PrintStream p = System.out;
  static Scanner s = new Scanner(System.in);
  public static void main(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      printHostMain();
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
          showListings();
          break;
        }
        case 2:
        {
          if(!addListing()){
            p.println ("Failed to add listing");
          } else {
            p.println ("Listing added successfully!");
          }
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
  public static void printHostMain(){
    p.println ("===========================");
    p.println ("=       Host Service      =");
    p.println ("===========================");
    p.println ("0 - Show My Bookings");
    p.println ("1 - Show My Listings");
    p.println ("2 - Create new Listings");
    p.println ("3 - Back");
    p.println ("===========================");
    p.println ("Please select:");
  }

  public static void showBookings(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      BookingDB.fetchHostBookings();
      p.println ("====================================================");
      p.println ("=                    Bookings                      =");
      p.println ("====================================================");
      BookingDB.showHostBookings();
      p.println ("===========================");
      p.println ("0 - Cancel Bookings");
      p.println ("1 - Comments on Bookings");
      p.println ("2 - See comments on the booking's renter");
      p.println ("3 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          if (!cancelBookings())
            p.println ("Failed to cancel Booking!");
          else
            p.println ("Booking cancelled successfully!");
          break;
        }
        case 1:
        {
          if(!commentOn()){
            p.println("Failed to comment on booking!");
          } else {
            p.println("Comment on booking Successfully!");
          }
          break;
        }
        case 2:
        {
          if(!showComments()){
            p.println("Failed to show comments");
          } else {
            p.println("Successfully show comments!");
          }
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

  public static void showListings(){
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      ListingDB.fetchListings();
      p.println ("===========================");
      p.println ("=        Listings         =");
      p.println ("===========================");
      ListingDB.showListings();
      p.println ("===========================");
      p.println ("0 - Add availability");
      p.println ("1 - See availability");
      p.println ("2 - Change availability price");
      p.println ("3 - Remove availability");
      p.println ("4 - Add amenities");
      p.println ("5 - Remove Listing");
      p.println ("6 - Back");
      p.println ("===========================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          if(!addAvailabilities()){
            p.println ("Failed to add availabilities");
          } else {
            p.println ("Availabilities added successfully!");
          }
          break;
        }
        case 1:
        {
          seeAvailability();
          break;
        }
        case 2:
        {
          if(!changeAvailability()){
            p.println ("Failed to change availabilities! Most likely due to it being booked!");
          } else {
            p.println ("Availabilities changed successfully!");
          }
          break;
        }
        case 3:
        {
          if(!removeAvailabilities()){
            p.println ("Failed to remove availabilities! Most likely due to it being booked!");
          } else {
            p.println ("Availabilities removed successfully!");
          }
          break;
        }
        case 4:
        {
          if(!addAmenities()){
            p.println ("Failed to add amenity");
          } else {
            p.println ("Amenity added successfully!");
          }
          break;
        }
        case 5:
        {
          removeListing();
          break;
        }
        case 6:
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
  public static void removeListing(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("You don't own listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }
    ListingDB.removeListing(listingID);
    p.println("Listing successfully removed from public!");
  }
  public static void printTypeSelection(){
    p.println("Select type of the listing:");
    p.println ("1 - Apartment");
    p.println ("2 - Condo");
    p.println ("3 - House");
  }

  public static boolean addListing(){
    s.nextLine();

    //Enter the address
    p.println("Enter the address:");
    String address = s.nextLine();
    while (true)
    {
      if (address.length() == 0)
        p.println ("Invalid address, address cannot be empty!");
      else if (ListingDB.isLocationActive(address))
        p.println ("The address has already been used by a listing");
      else break;
      p.println("Please enter the address of the listing");
      address = s.nextLine();
    }

    boolean locationResult = true;

    boolean isInactive = ListingDB.isLocationInactive(address);
    if(isInactive){
      p.println("The LocaionInfo already exists, so we skip the LocaionInfo.");
    } else {

      //Enter the latitude
      p.println("Please enter the latitude:");
      double latitude = s.nextDouble();
      while (true)
      {
        if (latitude < -90 || latitude > 90)
          p.println ("Invalid latitude!");
        else break;
        p.println("Please enter the latitude:");
        latitude = s.nextDouble();
      }

      //Enter longitude
      p.println("Please enter the longtitude:");
      double longitude = s.nextDouble();
      while (true)
      {
        if (longitude < -180 || longitude > 180)
          p.println ("Invalid longitude!");
        else break;
        p.println("Please enter the longitude:");
        longitude = s.nextDouble();
      }

      //Enter postal code
      p.println("Please enter the postal code:");
      s.nextLine();
      String postal = s.nextLine();
      while (true)
      {
        if (postal.length() == 0)
          p.println ("Invalid postal code, postal code cannot be empty!");
        else break;
        p.println("Please enter the postal code");
        postal = s.nextLine();
      }

      //Enter city
      p.println("Please enter the city:");
      String city = s.nextLine();
      while (true)
      {
        if (city.length() == 0)
          p.println ("Invalid city, city cannot be empty!");
        else break;
        p.println("Please enter the city:");
        city = s.nextLine();
      }

      //Enter country
      p.println("Please enter the country:");
      String country = s.nextLine();
      while (true)
      {
        if (country.length() == 0)
          p.println ("Invalid country, country cannot be empty!");
        else break;
        p.println("Please enter the country:");
        country = s.nextLine();
      }

      locationResult = ListingDB.addLocation(latitude, longitude, postal, address, city, country);
    }

    if(!locationResult){
      p.println("Invalid Location Information");
      return false;
    }

    //Select type
    printTypeSelection();
    int t = s.nextInt();
    String type = "";
    while (true)
    {
      boolean selected = false;
      switch (t)
      {
        case 1:
        {
          type = "Apartment";
          selected = true;
          break;
        }
        case 2:
        {
          type = "House";
          selected = true;
          break;
        }
        case 3:
        {
          type = "Condo";
          selected = true;
          break;
        }
        default:
        {
          p.println ("Please select a valid option!");
          break;
        }
      }
      if (selected) break;
      printTypeSelection();
      t = s.nextInt();
    }

    Listing List = ListingDB.addListing(address, type);
    Main.clearScreen();
    return ListingDB.linkListingToHost(List);
  }

  public static LocalDate enterStartDate(){
    try {
      System.out.println ("Please enter the startdate in the format of mm/dd/yyyy:");
      s.nextLine();
      String date = s.nextLine();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      return LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      System.out.println ("Invalid date!");
      return null;
    }
  }

  public static LocalDate enterEndDate(){
    try {
      System.out.println ("Please enter the enddate in the format of mm/dd/yyyy:");
      String date = s.nextLine();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      return LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      System.out.println ("Invalid date!");
      return null;
    }
  }

  public static boolean addAvailabilities(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("Cannot add availability to listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }

    //Enter the startDate
    LocalDate startdate = enterStartDate();
    while (true)
    {
      if (startdate != null) 
        break;
      startdate = enterStartDate();
    }

    //Enter the endDate
    LocalDate enddate = enterEndDate();
    while (true)
    {
      if (enddate != null) 
        break;
      enddate = enterEndDate();
    }
    Listing list = ListingDB.getListing(listingID);
    if (list != null)
      ListingDB.suggestPrice(startdate, enddate, list.getType());
    //Enter the price
    p.println("Please enter the price (for each day):");
    double price = s.nextDouble();
    while (true)
    {
      if (price <= 0)
        p.println ("Price cannot be less than 0!");
      else break;
      p.println("Please enter the price:");
      price = s.nextDouble();
    }


    if(!ListingDB.checkAvailability(listingID, startdate, enddate, false)){
      return false;
    }

    LocalDate date = startdate;
    boolean result = false;
    while(!date.isAfter(enddate)){
      result = ListingDB.addAvailability (listingID, java.sql.Date.valueOf(date), price);
      date = date.plusDays(1);
    }

    //Main.clearScreen();
    return result;
  }

  public static void seeAvailability(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("Cannot see availability of listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }

    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      ListingDB.seeAvailability(listingID);
      p.println ("===========================");
      p.println ("0 - Back");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
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

  public static boolean changeAvailability(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("Cannot change availability of listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }

    //Enter the startDate
    LocalDate startdate = enterStartDate();
    while (true)
    {
      if (startdate != null) 
        break;
      startdate = enterStartDate();
    }

    //Enter the endDate
    LocalDate enddate = enterEndDate();
    while (true)
    {
      if (enddate != null) 
        break;
      enddate = enterEndDate();
    }

    //Enter the price
    p.println("Please enter the new price:");
    int price = s.nextInt();
    while (true)
    {
      if (price <= 0)
        p.println ("Price cannot be less than 0!");
      else break;
      p.println("Please enter the price:");
      price = s.nextInt();
    }


    if(!ListingDB.checkAvailability(listingID, startdate, enddate, true)){
      return false;
    }

    LocalDate date = startdate;
    p.println(startdate);
    p.println(enddate);
    boolean result = false;
    while(!date.isAfter(enddate)){
      result = ListingDB.modifyAvailability(listingID, java.sql.Date.valueOf(date), price);
      date = date.plusDays(1);
    }

    Main.clearScreen();
    return result;
  }

  public static boolean removeAvailabilities(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("Cannot remove availability of listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }

    //Enter the startDate
    LocalDate startdate = enterStartDate();
    while (true)
    {
      if (startdate != null) 
        break;
      startdate = enterStartDate();
    }

    //Enter the endDate
    LocalDate enddate = enterEndDate();
    while (true)
    {
      if (enddate != null) 
        break;
      enddate = enterEndDate();
    }


    if(!ListingDB.checkAvailability(listingID, startdate, enddate, true)){
      return false;
    }

    LocalDate date = startdate;
    p.println(startdate);
    p.println(enddate);
    boolean result = false;
    while(!date.isAfter(enddate)){
      result = ListingDB.removeAvailability(listingID, java.sql.Date.valueOf(date));
      date = date.plusDays(1);
    }

    Main.clearScreen();
    return result;
  }

  public static boolean addAmenities(){
    s.nextLine();

    //Enter the listingID
    p.println("Please enter the listingID:");
    int listingID = s.nextInt();
    while (true)
    {
      if (!ListingDB.checkRelation(User.getInstance().getID(), listingID))
        p.println ("Cannot add amenity to listing " + listingID + ". Please select a valid listingID");
      else break;
      p.println("Please enter the listingID:");
      listingID = s.nextInt();
    }

    //Enter the amenities
    Listing list = ListingDB.getListing(listingID);
    if (list != null)
      ListingDB.suggestAmenities(list.getType(), listingID);
    p.println("Please enter the amenity:");
    s.nextLine();
    String amenity = s.nextLine();
    while (true)
    {
      if (amenity.isEmpty())
        p.println ("Invalid amenity! It cannot be blank!");
      else break;
      p.println("Please enter the amenity:");
      amenity = s.nextLine();
    }


    Main.clearScreen();
    return ListingDB.addAmenities(listingID, amenity);
  }

  public static boolean cancelBookings(){
    p.println ("Please specify the index of the booking you wish to delete:");
    int index = s.nextInt();
    Main.clearScreen();
    return BookingDB.cancelBooking(index);
  }

  public static boolean commentOn(){
    p.println ("Please specify the index of the booking you wish to comment on:");
    int index = s.nextInt();
    p.println("Please specify the rating you want to give from 0 to 10 in integer form:");
    int rating = s.nextInt();
    while (true)
    {
      if (rating < 0 || rating > 5)
        p.println ("Invalid rating!");
      else break;
      p.println("Please specify the rating you want to give from 0 to 10 in integer form:");
      rating = s.nextInt();
    }
    p.println("Please write down the comments:");
    String comment = s.nextLine();
    while (true)
    {
      if (comment.length() == 0)
        p.println ("Comments cannot be empty");
      else break;
      p.println("Please write down the comments:");
      comment = s.nextLine();;
    }
    Main.clearScreen();
    return BookingDB.comments(index, rating, comment);
  }

  public static boolean showComments(){
    p.println ("Please specify the index of the booking you wish to comment on:");
    int index = s.nextInt();
    Main.clearScreen();
    return BookingDB.seeComments(index);
  }
  
}

