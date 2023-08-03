import java.sql.*;
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
}