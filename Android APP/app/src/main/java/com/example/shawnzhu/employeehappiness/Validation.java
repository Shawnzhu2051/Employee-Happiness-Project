package com.example.shawnzhu.employeehappiness;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shawnzhu on 2017/11/24.
 */

public class Validation {

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isname(String name){
        if (name == null || "".equals(name)){
            return false;
        }
        return true;
    }

    public static boolean ispassword(String password){
        if (password == null || "".equals(password)){
            return false;
        }
        return true;
    }

    public static boolean isgender(String gender){
        if (gender == "-1" || "".equals(gender)){
            return false;
        }
        return true;
    }
}
