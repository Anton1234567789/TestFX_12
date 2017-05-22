package sample.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnections {

    public static Connection connection(){
        Connection  con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "url";
             String user = "username";
             String pass = "password";
             con = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBConnections.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(DBConnections.class.getName()).log(Level.SEVERE, null, e);
        }

        return con;
    }
}
