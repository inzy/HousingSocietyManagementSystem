/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Main.Utility_management;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author RtrSuahantNadkar
 */
public class Backup {

    public Backup(JFrame parent) {
        try {
            // TODO add your handling code here:
            JFileChooser jfc = new JFileChooser();
            jfc.setVisible(true);
            jfc.setDialogTitle("Select Backup Directory");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.showOpenDialog(parent);
            String path = jfc.getSelectedFile().toString();

            java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String backupdirectory = path + todaysDate.format((java.util.Calendar.getInstance()).getTime());
            Connection con = Others.Database.connect();
            CallableStatement cs = con.prepareCall("{call SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)}");
            cs.setString(1, backupdirectory);
            cs.execute();
            cs.close();
            JOptionPane.showMessageDialog(parent, "backed up database to " + backupdirectory);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Utility_management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
