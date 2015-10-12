/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author RtrSuahantNadkar
 */
public class Generate_receipt {

    public Generate_receipt(JFrame parent) {
        try {
            Connection con = Others.Database.connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select fid from flat");
            ArrayList<String> accode = new ArrayList<>();
            int bid = 0, count = 0;
            while (rs.next()) {
                accode.add(rs.getString("fid"));
            }
            boolean flag = true;
            JasperPrint mainjprint = null, jprint = null;
            for (int i = 0; i < accode.size(); i++) {
                HashMap hm = new HashMap();
                hm.put("accode", accode.get(i));
                hm.put("previousmonth", firstDayOfPreviousMonth());
                hm.put("ststus", "pending");
                String jasperFileName = Raise_all_bills.class.getResource("/Resource/Maintenance_receipt.jasper").toString().substring(6);
                if (flag == true) {
                    mainjprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, con);
                    flag = false;
                } else {
                    jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, con);
                    List pages = jprint.getPages();
                    for (int j = 0; j < pages.size(); j++) {
                        JRPrintPage object = (JRPrintPage) pages.get(j);
                        mainjprint.addPage(object);
                    }
                }
            }
            JOptionPane.showMessageDialog(parent,"Receipt sucessfully generated");
            JasperViewer jv = new JasperViewer(mainjprint, false);
            jv.setVisible(true);
            jv.toFront();
        } catch (ClassNotFoundException | SQLException | JRException | ParseException ex) {
            Logger.getLogger(Generate_receipt.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private java.sql.Date firstDayOfPreviousMonth() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        Date ddd = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(ddd);
        java.util.Date date = sdf.parse(strDate);
        java.sql.Date firstDateOfPreviousMonth = new java.sql.Date(date.getTime());
        return firstDateOfPreviousMonth;
    }

    public static void main(String op[]) {
        new Generate_receipt(null);
    }
}
