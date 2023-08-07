import java.sql.*;
public class User {
  private static User loggedUser;
  String email;
  int userID;
  String sin;
  String name;
  String address;
  Date birthdate;
  String occupation;
  public User (String sin, int userID, String email, String name, String address, Date birthdate, String occupation)
  {
    this.sin = sin;
    this.email = email;
    this.userID = userID;
    this.name = name;
    this.address = address;
    this.birthdate = birthdate;
    this.occupation = occupation;
  }
  public static void setInstance (String sin, int userID, String email, String name, String address, Date birthdate, String occupation)
  {
    if (loggedUser == null)
      loggedUser = new User(sin, userID, email, name, address, birthdate, occupation);
  } 
  public static void removeInstance ()
  {
    loggedUser = null;
  }
  public static User getInstance ()
  {
    return loggedUser;
  }
  public int getID ()
  {
    return this.userID;
  }
  public String getSin()
  {
    return this.sin;
  }
}
