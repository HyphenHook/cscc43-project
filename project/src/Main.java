// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


public class Main {
    public static void main(String[] args) {
        connectionSQL c = new connectionSQL("jdbc:mysql://localhost:3306/project", "root", "204x78y69");
        Availability a = new Availability();
        search s = new search();
        update up = new update();
        //a.changeAvailability(c, 1, 2023, 9 , 2, 2023, 9, 6, 1);
        //up.createListing(c, 4, "an entire room", 30.0, 20.0, "M1H3K2", "36 Military Trail", "Scarborough", "Canada");
        //a.insertAvailability(c, 7, 2023, 9, 1, 2023, 10, 1, 100);
        //a.changeAvailability(c, 7, 2023, 9, 1, 2023, 9, 30, 1);
        //a.checkAvailability(c, 1, 2023, 8, 31, 2023, 9, 1, 0);
        //a.checkAvailability(c, 1, 2023, 9, 2, 2023, 9, 7, 0);
        //a.changeAvailability(c, 1, 2023, 9, 2, 2023, 9, 7, 1);
        //a.checkAvailability(c, 1, 2023, 9, 2, 2023, 9, 7, 0);
        //a.updateAvailabilityPrice(c, 7, 2023, 9, 28, 2023, 9,30, 25);
        //a.booking(c, 7, 2023, 9, 1, 2023, 9, 5, 1);
        //up.RemoveListing(c, 7);
        //a.cancelBooking(c, 4, 1);
        System.out.println(s.getDistance(10, 10, 20, 20));
        //up.RemoveListing(c, 5);
    }
}