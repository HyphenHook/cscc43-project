import java.io.PrintStream;
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

          break;
        }
        case 1:
        {
          break;
        }
        case 2:
        {
          showPayments();
          break;
        }
        case 3:
        {
          break;
        }
        case 4:
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
    p.println ("1 - Search Listings to Book ");
    p.println ("2 - Show Payment Methods");
    p.println ("3 - Show Reports");
    p.println ("4 - Back");
    p.println ("===========================");
    p.println ("Please select:");
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
    p.println(result);
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
