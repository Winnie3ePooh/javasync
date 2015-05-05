package javasync;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    
    private static final String dbUrl = "jdbc:h2:~/javasync";

    public DB() {
        System.out.println("OK");
    }
    
    /**
     * Вспомогательный метод для закрытия соединений с БД.
     * @param closeable 
     */
    private static void closeQuietly(Connection closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (SQLException ex) {
                // ignore
            }
        }
    }

    static public void createDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
            Statement st = conn.createStatement();
            st.execute("create table people(id INT PRIMARY KEY AUTO_INCREMENT, login varchar(255), pass varchar(255))");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuietly(conn);
        }
    }

    static public boolean Checking(String log, String pass) {
        Connection conn = null;
        boolean check = false;
        Statement statement = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
            Statement st = conn.createStatement();
            st.execute("INSERT INTO people (login, pass) VALUES ('admin','admin')");
            ResultSet result = st.executeQuery("SELECT * FROM PEOPLE");
            while (result.next()) {
                if(result.getString("login").equals(log) && result.getString("pass").equals(pass)){
                    check = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuietly(conn);
        }
        return check;
    }
    
}
