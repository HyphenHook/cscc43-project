// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


public class Main {
    public static void main(String[] args) {
        update up = new update("jdbc:mysql://localhost:3306/project", "root", "204x78y69");
        up.insertAvailability(1, 2023, 9, 1, 2023, 10, 1, 100);
        up.checkAvailability(1, 2023, 8, 31, 2023, 9, 1);
        up.checkAvailability(1, 2023, 9, 2, 2023, 9, 7);
        up.booking(1, 2023, 9, 2, 2023, 9, 7, 2);
        up.checkAvailability(1, 2023, 9, 2, 2023, 9, 7);
    }
}