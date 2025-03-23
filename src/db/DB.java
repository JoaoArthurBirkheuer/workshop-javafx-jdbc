package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
    
    private static Connection conn = null;
    
    public static Connection getConnection() {
        if(conn == null) {
            // System.out.println("opened");
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                
                /*System.out.println("URL: " + url);
                System.out.println("User: " + props.getProperty("user"));
                System.out.println("Password: " + props.getProperty("password"));*/

                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);

            String user = System.getenv("MYSQL_USER");
            String password = System.getenv("MYSQL_PASSWORD");

            if (user != null && !user.isEmpty()) {
                props.setProperty("user", user);
            }
            if (password != null && !password.isEmpty()) {
                props.setProperty("password", password);
            }

            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                //System.out.println("closed");
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
    
    public static void closeStatement(Statement st) {
    	if( st != null) {
    		try {
    			st.close();
    		}
    		catch(SQLException e) {
    			throw new DbException (e.getMessage());
    		}
    	}
    }
    
    public static void closeResultSet(ResultSet rs) {
    	if( rs != null) {
    		try {
    			rs.close();
    		}
    		catch(SQLException e) {
    			throw new DbException (e.getMessage());
    		}
    	}
    }
}