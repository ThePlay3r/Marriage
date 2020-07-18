package me.pljr.marriage.utils;

import org.bukkit.ChatColor;

public class FormatUtil {
    public static String colorString(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String formatTime(long sec) {
        long seconds = sec % 60;
        long minutes = sec / 60;
        if (minutes >= 60) {
            long hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                long days = hours / 24;
                return String.format("§b%d dní, §b%02d hod. §b%02d min. §b%02d sek.", days, hours % 24, minutes, seconds);
            }
            return String.format("§b%02d hod. §b%02d min. §b%02d sek.", hours, minutes, seconds);
        }
        return String.format("§b00 hod. §b%02d min. §b%02d sek.", minutes, seconds);
    }
}
