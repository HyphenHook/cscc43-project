import java.io.PrintStream;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class Entry {
  static PrintStream p = System.out;
  static Scanner s = new Scanner(System.in);
  public static void main(){
    int input = 0;
    boolean leave = false;
    while (true)
    {
      printEntry();
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          Renter.main();
          break;
        }
        case 1:
        {
          Host.main();
          break;
        }
        case 2:
        {
          makeReport();
          break;
        }
        case 3:
        {
          Main.clearScreen();
          User.removeInstance();
          leave = true;
          break;
        }
        case 4:
        {
          Main.clearScreen();
          p.println("Goodbye!");
          reportTotalBooking();
          break;
        }
        case 5:
        {
          Main.clearScreen();
          if (UserDB.deleteInfo() && UserDB.deleteUser())
            p.println ("Account deleted!");
          else
            p.println ("Something gone wrong!");
          User.removeInstance();
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
      if (leave)
        break;
    }
  }
  public static void printEntry(){
    p.println ("===========================");
    p.println ("=      MyBnB Service      =");
    p.println ("===========================");
    p.println ("0 - Use Renter Service");
    p.println ("1 - Use Host Service");
    p.println ("2 - Show Reports");
    p.println ("3 - Logout");
    p.println ("4 - Exit");
    p.println ("5 - Delete Account");
    p.println ("===========================");
    p.println ("Please select:");
  }

  public static void makeReport () {
    Main.clearScreen();
    int input = 0;
    boolean leave = false;
    while (true)
    {
      p.println ("===============================");
      p.println ("=        Report Options       =");
      p.println ("===============================");
      p.println ("0 - report total number of bookings");
      p.println ("1 - report total number of listings");
      p.println ("2 - rank hosts acccording to the number of listings they have");
      p.println ("3 - report likely commercial host");
      p.println ("4 - rank renters acccording to the number of bookings they have");
      p.println ("5 - report hosts and renters that have max number of cancellation in a year");
      p.println ("6 - Back");
      p.println ("================================");
      p.println ("Please select:");
      input = s.nextInt();
      switch (input)
      {
        case 0:
        {
          reportTotalBooking();
          break;
        }
        case 1:
        {
          reportTotalListing();
          break;
        }
        case 2:
        {
          reportRankingHost();
          break;
        }
        case 3:
        {
          reportLikelyCommercialHost();
          break;
        }
        case 4:
        {
          reportRenterRank();
          break;
        }
        case 5:
        {
          reportMaxCancel();
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

  public static LocalDate enterStartDate(){
    try {
      System.out.println ("Please enter the startdate in the format of mm/dd/yyyy:");
      String date = s.nextLine().trim();
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
      String date = s.nextLine().trim();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      return LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      System.out.println ("Invalid date!");
      return null;
    }
  }


  public static void reportTotalBooking() {
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

    p.println("Enter the city if you want to report by postal code inside it. Otherwise just press Enter: ");
    String city = s.nextLine();
    
    if (city.length() == 0){
      Report.reportTotalBooking(startdate, enddate, "", false);
    } else {
      Report.reportTotalBooking(startdate, enddate, city, true);
    }

      
  }

  public static void printTotalListingTypeSelection(){
    p.println("Select type of the report:");
    p.println ("1 - Report by country");
    p.println ("2 - Report by city inside country");
    p.println ("3 - Report by postal code inside city");
  }

  public static void reportTotalListing() {
    
    
    //Select type
    printTotalListingTypeSelection();
    int t = s.nextInt();
    int result = 0;
    while (true)
    {
      boolean selected = false;
      switch (t)
      {
        case 1:
        {
          result = 1;
          selected = true;
          break;
        }
        case 2:
        {
          result = 2;
          selected = true;
          break;
        }
        case 3:
        {
          result = 3;
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
      printTotalListingTypeSelection();
      t = s.nextInt();

    }
    Report.reportTotalListing(result);

      
  }

  public static void reportRankingHost(){
    p.println("Please enter 0 for rank per country or 1 for rank per city");
    int t = s.nextInt();
    boolean inCity = false;
    while (true)
    {
      boolean selected = false;
      switch (t)
      {
        case 0:
        {
          inCity = false;
          selected = true;
          break;
        }
        case 1:
        {
          inCity = true;
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
      p.println("Please enter 0 for rank per country or 1 for rank per city");
      t = s.nextInt();

    }
    Report.reportTotalHostListingRank(inCity);
  }

  public static void reportLikelyCommercialHost(){
    p.println("Please enter 0 for find by country or 1 for find by city");
    int t = s.nextInt();
    boolean inCity = false;
    while (true)
    {
      boolean selected = false;
      switch (t)
      {
        case 0:
        {
          inCity = false;
          selected = true;
          break;
        }
        case 1:
        {
          inCity = true;
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
      p.println("Please enter 0 for find by country or 1 for find by  city");
      t = s.nextInt();

    }
    Report.reportLikelyCommercialHost(inCity);;
  }

  public static void reportRenterRank() {
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

    p.println("Enter 0 to rank renters by city, or any other integer to rank renters by their total bookings worldwide");
    int city = s.nextInt();
    
    if (city == 0){
      Report.reportRenterRank(startdate, enddate, true);
    } else {
      Report.reportRenterRank(startdate, enddate, false);
    }

      
  }

  public static void reportMaxCancel() {
    
    p.println("Please enter the year you want to report on: ");
    int t = s.nextInt();
   
    while (true)
      {
        if (t <= 2015)
          p.println ("Invalid year option!");
        else break;
        p.println("Please enter the year you want to report on: ");
        t = s.nextInt();
      }
    Report.reportMaxCancel(t);

      
  }

}
