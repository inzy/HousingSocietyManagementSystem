/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Others;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author RtrSuahantNadkar
 */
public class Database {
    public static String dbDirectory = "E:\\Others\\Himalaya\\Database";
    public static Connection connect() throws ClassNotFoundException, SQLException {
        //Properties p = System.getProperties();
        //String path = System.getenv("ProgramFiles")+"\\hamalaya\\database";
        //p.setProperty("derby.system.home",path);
        //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        //Connection con = DriverManager.getConnection("jdbc:derby:E:\\Others\\Himalaya\\Database\\himalaya");
        Class.forName("org.h2.Driver");
        Connection con = DriverManager.getConnection("jdbc:h2:" + dbDirectory + "\\him","","");
        return con;
    }
    
    public static void main(String op[]) throws ClassNotFoundException, SQLException {
        Connection c = Database.connect();
    }
}
