import java.sql.*;
public class PaymentDB {
  public static int addCreditCard (String cardnumber, Date expirydate, String holdername)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO CreditCard(cardnumber, expirydate, holdername) VALUES (?, ?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setString(1, cardnumber);
        s.setDate(2, expirydate);
        int result = -1;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            result = r.getInt("cardID");
          r.close();
        }
        s.close();
        con.close();
        return result;
      }
      return -1;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return -1;
    }
  }
}
