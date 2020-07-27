package me.pljr.marriage.managers;

import me.pljr.marriage.enums.Gender;
import org.bukkit.Location;

public class PlayerManager {
    private Gender gender;
    private String partner;
    private boolean pvp;
    private long lastseen;
    private Location home;

    public PlayerManager(Gender gender, String partner, boolean pvp, long lastseen, Location home){
        this.gender = gender;
        this.partner = partner;
        this.pvp = pvp;
        this.lastseen = lastseen;
        this.home = home;
    }

    public void setHome(Location home) {
        this.home = home;
    }
    public Location getHome() {
        return home;
    }

    public void setLastseen(long lastseen) {
        this.lastseen = lastseen;
    }
    public long getLastseen() {
        return lastseen;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }
    public boolean isPvp() {
        return pvp;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Gender getGender() {
        return gender;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
    public String getPartner() {
        return partner;
    }
}
