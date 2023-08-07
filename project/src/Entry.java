import java.io.PrintStream;
import java.util.Scanner;

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
          Main.clearScreen();
          User.removeInstance();
          leave = true;
          break;
        }
        case 3:
        {
          Main.clearScreen();
          p.println("Goodbye!");
          System.exit(0);
          break;
        }
        case 4:
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
    p.println ("2 - Logout");
    p.println ("3 - Exit");
    p.println ("4 - Delete Account");
    p.println ("===========================");
    p.println ("Please select:");
  }
}
