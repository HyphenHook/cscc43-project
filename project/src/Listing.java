public class Listing {
  int listingID;
  String type;
  String address;
  String status;
  public Listing(int listingID, String type, String address, String status)
  {
    this.listingID = listingID;
    this.type = type;
    this.address = address;
    this.status = status;
  }
  public int getID()
  {
    return this.listingID;
  }

  public String getType(){
    return this.type;
  }

  public String getAddress(){
    return this.address;
  }
}
