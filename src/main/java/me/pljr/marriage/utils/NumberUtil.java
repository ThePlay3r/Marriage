package me.pljr.marriage.utils;

public class NumberUtil {
    public static boolean isInt(String number){
        try {
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
