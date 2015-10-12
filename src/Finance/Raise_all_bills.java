/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finance;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class Raise_all_bills {

    public Raise_all_bills(JFrame parent) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        int ans = JOptionPane.showConfirmDialog(parent, "Raise bill for the month of " + sdf.format(new Date()));
        if (ans == 0) {
            try {
                Connection con = Others.Database.connect();
                Statement st = con.createStatement();
                PreparedStatement ps;
                ResultSet rs = st.executeQuery("select fid from flat");
                ArrayList<String> accode = new ArrayList<>();
                int bid = 0, count = 0;
                while (rs.next()) {
                    accode.add(rs.getString("fid"));
                }
                boolean flag = true;
                JasperPrint mainjprint = null,jprint = null;
                for (int i = 0; i < accode.size(); i++) {
                    rs = st.executeQuery("select max(bid) from bills");
                    while (rs.next()) {
                        bid = rs.getInt(1);
                    }
                    String fid = accode.get(i);
                    rs = st.executeQuery("select * from flat where fid = '" + fid + "'");
                    rs.next();
                    int maintainence_amount = rs.getInt("total");
                    rs = st.executeQuery("select * from bills where ac_code = '" + fid + "' and bill_period_start = '" + firstDayOfPreviousMonth() + "'");
                    rs.next();
                    int balance_amount = rs.getInt("balance_amount");
                    rs = st.executeQuery("select interest_charged from administrator");
                    rs.next();
                    int rate = rs.getInt("interest_charged");
                    int interest_amount;
                    if (balance_amount > 0) {
                        interest_amount = (maintainence_amount * 1 * rate) / 100;
                    } else {
                        interest_amount = 0;
                    }
                    balance_amount += interest_amount;
                    ps = con.prepareStatement("insert into bills values(?,?,?,?,?,?,?,?,?,?)");
                    ps.setInt(1, bid + 1);
                    ps.setString(2, fid);
                    Date dd = new Date();
                    sdf = new SimpleDateFormat("ddMMyyyy");
                    String strDate = sdf.format(dd);
                    java.util.Date date = sdf.parse(strDate);
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    ps.setDate(3, sqlDate);
                    ps.setDate(4, firstDayOfMonth());
                    ps.setDate(5, lastDayOfMonth());
                    ps.setInt(6, maintainence_amount + interest_amount);
                    ps.setInt(7, 0);
                    ps.setString(8, "pending");
                    ps.setInt(9, maintainence_amount + balance_amount);
                    ps.setInt(10, interest_amount);
                    ps.execute();
                    count++;
                    //Generating reports
                    HashMap hm = new HashMap();
                    hm.put("accode", fid);
                    String jasperFileName = Raise_all_bills.class.getResource("/Resource/Maintenance_bill.jasper").toString().substring(6);
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
                if (count == accode.size()) {
                    JOptionPane.showMessageDialog(parent, "All bills raised sucessfully");
                }
                JasperViewer jv = new JasperViewer(mainjprint,false);
                jv.setVisible(true);
                jv.toFront();
            } catch (ClassNotFoundException | SQLException | ParseException | JRException ex) {
                Logger.getLogger(Raise_all_bills.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private java.sql.Date firstDayOfMonth() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date dd = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(dd);
        java.util.Date date = sdf.parse(strDate);
        java.sql.Date firstDateOfMonth = new java.sql.Date(date.getTime());
        return firstDateOfMonth;
    }

    private java.sql.Date lastDayOfMonth() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date ddd = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(ddd);
        java.util.Date date = sdf.parse(strDate);
        java.sql.Date lastDateOfMonth = new java.sql.Date(date.getTime());
        return lastDateOfMonth;
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
        new Raise_all_bills(null);
    }
}
