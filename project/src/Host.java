import java.io.PrintStream;
import java.util.Scanner;
import java.sql.*;
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
          break;
        }
        case 1:
        {
          break;
        }
        case 2:
        {
          
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
  public static void printHostMain(){
    p.println ("===========================");
    p.println ("=       Host Service      =");
    p.println ("===========================");
    p.println ("0 - Show My Bookings");
    p.println ("1 - Show My Listings");
    p.println ("2 - Create new Listings");
    p.println ("3 - Show Reports");
    p.println ("4 - Back");
    p.println ("===========================");
    p.println ("Please select:");
  }
}
