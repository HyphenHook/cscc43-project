import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
public class Main {
  static Scanner s = new Scanner(System.in);
  private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  public static void main(String[] args) {
    Connector.setInstance("jdbc:mysql://localhost:3306/mybnb", "root", "204x78y69");
    clearScreen();
    authMenu();
  }

  public static void authMenu (){
    int input = 0;
    boolean success = false;
    while (true)
    {
      while (true)
      {
        printAuthMenu();
        input = s.nextInt();
        switch (input)
        {
          case 0:
          {
            success = login();
            break;
          }
          case 1:
          {
            success = register();
            break;
          }
          case 2:
          {
            clearScreen();
            System.out.println("Goodbye!");
            System.exit(0);
            break;
          }
          default:
          {
            clearScreen();
            System.out.println ("Please select a valid option!");
            break;
          }
        }
        if (success)
          break;
      }
      if (User.getInstance() != null)
        Entry.main();
    }
  }

  public static boolean makeAccount ()
  {
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    s.nextLine();
    while (true)
    {
      System.out.println ("===========================");
      System.out.println ("=     Register Service    =");
      System.out.println ("===========================");
      System.out.println ("Please enter your email:");
      String email = s.nextLine();
      if (!pattern.matcher(email).matches())
      {
        clearScreen();
        System.out.println ("Please enter a proper email!");
        continue;
      }
      if (!UserDB.checkEmailUnique(email))
      {
        clearScreen();
        System.out.println ("Email already exists!");
        continue;
      }
      System.out.println("Please enter your password:");
      String password = s.nextLine();
      if (password.length() == 0)
      {
        clearScreen();
        System.out.println ("Password cannot be empty!");
        continue;
      }
      String info = getInfo();
      while (info == null)
      {
        System.out.println ("Something gone wrong! Please try again!");
        info = getInfo();
      }
      if (UserDB.addUser (email, password, info))
      {
        if (UserDB.logUser(email, password) != null)
        {
          clearScreen();
          System.out.println ("Successfully registered!");
          return true;
        }
        else
        {
          clearScreen();
          System.out.println ("Something gone wrong! Please login!");
          return false;
        }
      }
      return false;
    }
  }
  public static String getInfo ()
  {
    System.out.println ("Please enter your SIN:");
    String sin = s.nextLine();
    while (true)
    {
      if (UserDB.isSinExist(sin))
        System.out.println ("SIN already exists!");
      else if (sin.length() < 9 || sin.length() > 9 || !sin.matches("[0-9]+"))
        System.out.println ("This SIN is invalid!");
      else break;
      System.out.println ("Please enter your SIN:");
      sin = s.nextLine();
    }
    System.out.println ("Please enter your full name:");
    String name = s.nextLine();
    while (true)
    {
      if (name.isEmpty())
        System.out.println ("Name cannot be empty!");
      else break;
      System.out.println ("Please enter your full name:");
      name = s.nextLine();
    }
    System.out.println ("Please enter your address:");
    String address = s.nextLine();
    while (true)
    {
      if (address.isEmpty())
        System.out.println ("Address cannot be empty!");
      else break;
      System.out.println ("Please enter your address:");
      address = s.nextLine();
    }
    Date birthdate = enterDate();
    while (true)
    {
      if (birthdate != null && !isLegal(birthdate))
        System.out.println ("You are not 18+! Perhaps you mistaken your birthday!");
      else if (birthdate != null) break;
      birthdate = enterDate();
    }
    System.out.println ("Please enter your occupation:");
    String occupation = s.nextLine();
    while (true)
    {
      if (occupation.isEmpty())
        System.out.println ("Occupation cannot be empty!");
      else break;
      System.out.println ("Please enter your occupation:");
      occupation = s.nextLine();
    } 
    if (!UserDB.addInfo (sin, name, address, birthdate, occupation))
      return null;
    return sin;
  }
  public static boolean isLegal(Date d)
  {
    LocalDate curr = LocalDate.now();
    LocalDate ld = d.toLocalDate();
    LocalDate past = curr.minusYears(18);
    return ld.isBefore(past) || ld.equals(past);
  }
  public static Date enterDate(){
    try {
      System.out.println ("Please enter your birthday in the format of dd/mm/yyyy:");
      String date = s.nextLine().trim();
      java.util.Date obj = new SimpleDateFormat ("dd/MM/yyyy").parse(date);
      return new Date(obj.getTime());
    } catch (ParseException e) {
      System.out.println ("Invalid date!");
      return null;
    }
  }
  public static boolean register (){
    clearScreen();
    boolean success = false;
    boolean leave = false;
    while (true)
    {
      success = makeAccount();
      if (success) leave = true;
      else System.out.println ("Something has gone wrong! Try again!");
      if (leave) break;
    }
    return success;
  }
  public static boolean login (){
    clearScreen();
    boolean success = false;
    boolean leave = false;
    while (true)
    {
      System.out.println ("===========================");
      System.out.println ("=      Login Service      =");
      System.out.println ("===========================");
      System.out.println ("Please enter your email:");
      s.nextLine();
      String email = s.nextLine();
      System.out.println("Please enter your password:");
      String password = s.nextLine();
      if (UserDB.logUser(email, password) != null)
      {
        clearScreen();
        System.out.println ("Login succeeded!");
        leave = true;
        success = true;
      }
      else
      {
        System.out.println ("===========================");
        System.out.println ("Login failed! Please check your credentials!");
        System.out.println ("Would you like to try again? 1 - Yes | Other - No");
        int i = s.nextInt();
        switch (i)
        {
          case 1:
          {
            clearScreen();
            break;
          }
          default:
          {
            clearScreen();
            leave = true;
            break;
          }
        }
      }
      if (leave) break;
    }
    return success;
  }
  public static void clearScreen(){
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
  }
  public static void printAuthMenu (){
    System.out.println ("===========================");
    System.out.println ("=      MyBnB Service      =");
    System.out.println ("===========================");
    System.out.println ("0 - Login to Account");
    System.out.println ("1 - Register an Account");
    System.out.println ("2 - Exit");
    System.out.println ("===========================");
    System.out.println ("Please select:");
  }
}