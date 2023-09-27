
package universidadulp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static String server = "localhost";
    private static String port = "3306";
    private static String database = "universidad_ulp";
    private static String timeZone = "America/Argentina/Cordoba";
    private static String userName = "root";
    private static String password = "@270Jorge571";
    private static String driverClassName = "com.mysql.cj.jdbc.Driver";
    
    private static String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?serverTimezone=" + timeZone;
    
    private static Connection connection;
    
    public static Connection getInstance() throws SQLException{
        
        if (connection == null || connection.isClosed()) {
            // Utilizamos el nuevo nombre de clase del controlador
            
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found");
            }
            
            connection = DriverManager.getConnection(url, userName, password);
        }
        
        return connection;
        
    }
    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection aConnection) {
        connection = aConnection;
    }
    
}
