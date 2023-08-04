import java.sql.*;

public class Connector {
  private static Connector instance;
  private String url;
  private String username;
  private String password;
  private Connector (String url, String username, String password)
  {
    this.url = url;
    this.username = username;
    this.password = password;
  }
  public static void setInstance (String url, String username, String password)
  {
    if (instance == null)
      instance = new Connector(url, username, password);
  }
  public static Connection getConnection ()
  {
    try {
      Connection connect = DriverManager.getConnection(instance.url, instance.username, instance.password);
      return connect;
    }
    catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
