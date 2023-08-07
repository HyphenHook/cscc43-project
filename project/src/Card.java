import java.sql.*;
public class Card {
  int cardID;
  String cardnumber;
  Date expirydate;
  public Card (int cardID, String cardnumber, Date expiryDate)
  {
    this.cardID = cardID;
    this.cardnumber = cardnumber;
    this.expirydate = expiryDate;
  }
  public String getCard ()
  {
    return this.cardnumber;
  }
  public Date getExpireDate ()
  {
    return this.expirydate;
  }
  public int getID ()
  {
    return this.cardID;
  }
}
