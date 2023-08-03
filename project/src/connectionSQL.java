import java.sql.DriverManager;

public class connectionSQL extends DriverManager {
    public String jdbcUrl;

    public String username;

    public String userpassword;

    public connectionSQL(String jdbcUrl, String username, String userpassword) {
        this.super();
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.userpassword = userpassword;
    }

}
