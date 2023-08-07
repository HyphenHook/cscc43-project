import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class PaymentDB {
  private static ArrayList<Card> list;
  
  public static int addCreditCard (String cardnumber, Date expirydate, String holdername)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO CreditCard(cardnumber, expirydate, holdername) VALUES (?, ?, ?)";
        PreparedStatement s = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        s.setString(1, cardnumber);
        s.setDate(2, expirydate);
        s.setString(3, holdername);
        int result = -1;
        if (s.executeUpdate() >= 1)
        {
          ResultSet r = s.getGeneratedKeys();
          if (r.next())
            result = r.getInt(1);
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
  public static boolean linkCardWithUser (int cardID)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "INSERT INTO PaymentMethod (userID, cardID) VALUES (?, ?)";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        s.setInt(2, cardID);
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
  public static boolean isDupedCard (String cardnumber)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM PaymentMethod INNER JOIN CreditCard ON PaymentMethod.cardID = CreditCard.cardID WHERE userID = ? AND cardnumber = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        s.setString(2, cardnumber);
        ResultSet r = s.executeQuery();
        boolean success = r.next();
        r.close();
        s.close();
        con.close();
        return success;
      }
      return true;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return true;
    }
  }
  public static boolean removeCreditCard (int cardID)
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "DELETE FROM CreditCard WHERE cardID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, cardID);
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
  public static void fetchPaymentMethods ()
  {
    try{
      Connection con = Connector.getConnection();
      if (con != null)
      {
        String query = "SELECT * FROM PaymentMethod INNER JOIN CreditCard ON PaymentMethod.cardID = CreditCard.cardID WHERE userID = ?";
        PreparedStatement s = con.prepareStatement(query);
        s.setInt(1, User.getInstance().getID());
        ResultSet r = s.executeQuery();
        list = new ArrayList<>();
        while (r.next())
          list.add (new Card(r.getInt("cardID"), r.getString("cardnumber"), r.getDate("expirydate")));
        r.close();
        s.close();
        con.close();
      }
    }
    catch (SQLException e)
    {
      return;
    }
  }
  public static void showPaymentMethods ()
  {
    if (list.isEmpty())
    {
      System.out.println ("You have no card! Please add one!");
      return;
    }
    int j = 0;
    DateFormat df = new SimpleDateFormat("MM/yyyy");
    for (Card i: list)
    {
      System.out.println(j++ + ". ****" + i.getCard().substring(i.getCard().length() - 4) + " | " + df.format(i.getExpireDate()));
    }
  }
  public static String useCreditCard (int index)
  {
    if (list.isEmpty())
    {
      System.out.println ("You have no card to use! Please add one!");
      return null;
    }
    if (index >= list.size() || index < 0)
    {
      System.out.println ("You don't have a card at that position!");
      return null;
    }
    Card ref = list.get(index);
    if (isExpired(ref.getExpireDate()))
    {
      System.out.println ("The card has already expired!");
      return null;
    }
    return ref.getCard().substring(ref.getCard().length() - 4);
  }
  public static boolean isExpired (Date d)
  {
    Date cur = new Date (System.currentTimeMillis());
    return d.before (cur);
  }
  public static boolean deleteCreditCard (int index)
  {
    if (index >= list.size() || index < 0)
    {
      System.out.println ("You don't have a card at that position!");
      return false;
    }
    return removeCreditCard(list.get(index).getID());
  }
}
