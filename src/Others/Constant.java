/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author RtrSuahantNadkar
 */
public class Constant {
    public static Pattern EMAIL = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    public static Pattern ALPHANUM = Pattern.compile("^[A-Za-z0-9 ]+$");
    public static Pattern ALPHA = Pattern.compile("^[a-zA-Z0-9 ]+$");
    public static Pattern NUM = Pattern.compile("^[0-9]+$");
    
    
    /*public static void main(String op[]) {
    System.out.println(email.matcher("ksd.jc.hh").matches());
    }*/
    
}
