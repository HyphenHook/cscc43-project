public class Main {
    public static void main(String[] args) {
        connectionSQL c = new connectionSQL("jdbc:mysql://localhost:3306/mybnb", "root", "204x78y69");
        Availability a = new Availability();
        search s = new search();
        update up = new update();
        /*up.addAccount(c, "Jim", "300 Happy Street", "2001-01-01", "Student", "renter",
                "123456", "aaa@gmail.com", "1234567");*/
        //up.deleteAccount(c, 2);
        //up.deleteAccount(c, 1);
        //a.changeAvailability(c, 1, 2023, 9 , 2, 2023, 9, 6, 1);

        /*up.createListing(c, 1, "an entire room", 30.0, 20.0, "M1H3K2",
                "36 Military Trail", "Scarborough", "Canada");
        up.createListing(c, 1, "an entire room", 40.0, 40.0, "M1C1A4",
                "39 Military Trail", "Scarborough", "Canada");
        up.createListing(c, 1, "an entire room", 39.0, 39.0, "M1C1B4",
                "40 Military Trail", "Scarborough", "Canada");*/
        /*a.insertAvailability(c, 1, 2023, 8, 31, 2023, 9, 3, 100);
        a.insertAvailability(c, 2, 2023, 9, 1, 2023, 9, 7, 100);
        a.insertAvailability(c, 3, 2023, 8, 31, 2023, 10, 1, 100);*/
        //a.updateAvailabilityPrice(c, 3, 2023, 9, 3, 2023, 9, 8, 0.0);
        String[] Empty = {};
        s.searchByLocation(c, 35, 35, -1,
                2023, 9, 1, 2023, 9, 7, 300, Empty, false);
        s.searchByPostalCode(c, "M1C1A4");
        s.searchByAddress(c, "39 Military Trail");
        s.searchByAddress(c, "36 Lee Centre Dr");
        a.getTotalPrice(c, 1, 2023, 9, 1, 2023, 9, 7);
        a.getTotalPrice(c, 2, 2023, 9, 1, 2023, 9, 7);
        a.getTotalPrice(c, 3, 2023, 9, 1, 2023, 9, 7);
        String[] Amenities = {"TV"};
        s.checkAmenities(c, 1, Amenities);
        //up.RemoveListing(c, 1);
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
        //up.RemoveListing(c, 5);
    }
}