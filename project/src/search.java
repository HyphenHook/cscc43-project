import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class search {
    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        distance = Math.toDegrees(Math.acos(distance));
        double result = distance * 60 * 1.1515 * 1.60934;
        return result;
    }

    public void searchByLocation(connectionSQL c, double lat, double lon, double dist, int startyear, int startmonth,
                                 int startday, int endyear, int endmonth, int endday, double bound, String[] Amenities,
                                 boolean byDistance) {
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT listingID, latitude, longitude FROM Listing NATURAL JOIN LocationInfo";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Integer, Double> Listings = new HashMap<>();
            Availability a= new Availability();

            while (resultSet.next()) {
                int listingID = resultSet.getInt("listingID");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");

                double distance = getDistance(lat, lon, latitude, longitude);
                if ((dist <= 0 || distance <= dist) && (a.checkAvailability(c, listingID, startyear, startmonth,
                        startday, endyear, endmonth, endday, 0) && a.checkPrice(c, listingID, startyear,
                        startmonth, startday, endyear, endmonth, endday, bound))
                        && checkAmenities(c, listingID, Amenities)){
                    if (byDistance) {
                        Listings.put(listingID, distance);
                    } else {
                        double price = a.getTotalPrice(c, listingID, startyear, startmonth, startday,
                                endyear, endmonth, endday);
                        Listings.put(listingID, price);
                    }
                }

            }

            String sentence =  ", Distance to the given point: ";
            if(!byDistance){
                sentence =  ", prices for given period: ";
            }

            resultSet.close();
            preparedStatement.close();

            if(byDistance) {
                Listings.entrySet().stream().sorted(Map.Entry.comparingByValue())
                        .forEach(entry -> System.out.println("ListingID: " + entry.getKey() +
                                ", Distance to the given point: " + String.format("%.2f", entry.getValue())));
            } else {
                Listings.entrySet().stream().sorted(Map.Entry.comparingByValue())
                        .forEach(entry -> System.out.println("ListingID: " + entry.getKey() +
                                ", prices in given period of time: " + String.format("%.2f", entry.getValue())));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void searchByPostalCode(connectionSQL c, String postal){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT listingID, postalcode FROM Listing NATURAL JOIN LocationInfo WHERE postalcode LIKE ?";

            String prefix = getPrefix(postal);
            if (prefix.equals("Error")){
                System.out.println("The input should have length at least 3");
            } else {

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, prefix + "%");
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int listingID = resultSet.getInt("listingID");
                    String postalCode = resultSet.getString("postalcode");

                    System.out.println("Listing ID: " + listingID + ", postal code: " + postalCode);
                }

                resultSet.close();
                preparedStatement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void searchByAddress(connectionSQL c, String address){
        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT listingID, address FROM Listing WHERE address = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                int listingID = resultSet.getInt("listingID");
                String Address = resultSet.getString("address");

                System.out.println("Listing ID: " + listingID + ", address: " + Address);
            } else {
                System.out.println("Listing with address " + address + " does not exist or not" +
                        "available at that time.");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public String getPrefix(String input) {
        if (input == null || input.length() < 3) {
            return "Error";
        }

        String result = input.substring(0, 3);
        return result;
    }

    public boolean checkAmenities(connectionSQL c, int listingID, String[] Amenities){
        if (Amenities.length == 0){
            return true;
        }

        try (Connection connection = DriverManager.getConnection(c.jdbcUrl, c.username, c.userpassword)) {
            String sqlQuery = "SELECT * FROM Amenities Where name = ? AND listingID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            for(String amenity : Amenities) {
                preparedStatement.setString(1, amenity);
                preparedStatement.setInt(2, listingID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()){
                    System.out.println("Listing with ID " + listingID + " lack amenity " + amenity);
                    preparedStatement.close();
                    return false;
                }
            }

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection failed.");
        return false;
    }
}