public class Main {
    public static void main(String[] args) {
        Connector.setInstance("jdbc:mysql://localhost:3306/mybnb", "root", "");
        Connector.getConnection();
        System.out.println("Test");
        
    }
}