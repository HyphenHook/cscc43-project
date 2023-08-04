import java.sql.*;
public class User {
  private static User loggedUser;
  String email;
  int userID;
  String type;
  String name;
  String address;
  Date birthdate;
  String occupation;
  public User (int userID, String email, String type, String name, String address, Date birthdate, String occupation)
  {
    this.email = email;
    this.userID = userID;
    this.type = type;
    this.name = name;
    this.address = address;
    this.birthdate = birthdate;
    this.occupation = occupation;
  }
  public static void setInstance (int userID, String email, String type, String name, String address, Date birthdate, String occupation)
  {
    if (loggedUser == null)
      loggedUser = new User(userID, email, type, name, address, birthdate, occupation);
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
  public String getType ()
  {
    return this.type;
  }
}
