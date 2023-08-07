import java.sql.*;

public class UserDB {
  public static boolean addInfo (String sin, String name, String address, Date birthdate, String occupation)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO PersonalInfo(sin, name, address, birthdate, occupation) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, sin);
        s.setString(2, name);
        s.setString(3, address);
        s.setDate(4, birthdate);
        s.setString(5, occupation);
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLIntegrityConstraintViolationException e)
    {
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean isSinExist (String sin)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM PersonalInfo WHERE sin = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, sin);
        ResultSet r = s.executeQuery();
        boolean result = r.next();
        r.close();
        s.close();
        con.close();
        return result;
      }
      return true;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return true;
    }
  }
  public static boolean addUser (String email, String password, String sin)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO User(sin, email, password) VALUES (?, ?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, sin);
        s.setString(2, email);
        s.setString(3, password);
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean checkEmailUnique (String email)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT userID FROM User WHERE email = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, email);
        ResultSet r = s.executeQuery();
        boolean result = !r.next();
        r.close();
        s.close();
        con.close();
        return result;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public static User logUser (String email, String password)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM User INNER JOIN PersonalInfo ON User.sin = PersonalInfo.sin WHERE email = ? AND password = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, email);
        s.setString(2, password);
        ResultSet r = s.executeQuery();
        User instance = null;
        if (r.next())
        {
          User.setInstance (r.getString ("sin"), r.getInt("userID"), r.getString("email"), r.getString("name"), r.getString("address"), r.getDate("birthdate"), r.getString("occupation"));
          instance = User.getInstance();
        }
        r.close();
        s.close();
        con.close();
        return instance;
      }
      return null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  public static boolean deleteInfo ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "DELETE FROM PersonalInfo WHERE sin = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setString (1, User.getInstance().getSin());
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean deleteUser ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "UPDATE User SET email = NULL WHERE userID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt (1, User.getInstance().getID());
        boolean success = s.executeUpdate() >= 1;
        s.close();
        con.close();
        return success;
      }
      return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
}
