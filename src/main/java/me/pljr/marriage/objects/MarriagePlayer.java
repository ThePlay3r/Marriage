package me.pljr.marriage.objects;

import me.pljr.marriage.config.Gender;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarriagePlayer {
    private final UUID uniqueId;
    private Gender gender;
    private UUID partnerID;
    private boolean pvp;
    private long lastSeen;
    private Location home;
    private boolean spy;
    private boolean sharedFood;
    private boolean sharedXP;
    private final List<UUID> requests;

    public MarriagePlayer(UUID uniqueId){
        this.uniqueId = uniqueId;
        this.gender = Gender.NONE;
        this.partnerID = null;
        this.pvp = false;
        this.lastSeen = System.currentTimeMillis();
        this.home = null;
        this.spy = false;
        this.sharedFood = true;
        this.sharedXP = true;
        this.requests = new ArrayList<>();
    }

    public MarriagePlayer(UUID uniqueId, Gender gender, UUID partnerID, boolean pvp, long lastSeen, Location home, boolean spy, boolean sharedFood, boolean sharedXP) {
        this.uniqueId = uniqueId;
        this.gender = gender;
        this.partnerID = partnerID;
        this.pvp = pvp;
        this.lastSeen = lastSeen;
        this.home = home;
        this.spy = spy;
        this.sharedFood = sharedFood;
        this.sharedXP = sharedXP;
        this.requests = new ArrayList<>();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UUID getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(UUID partnerID) {
        this.partnerID = partnerID;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public boolean isSpy() {
        return spy;
    }

    public void setSpy(boolean spy) {
        this.spy = spy;
    }

    public boolean isSharedFood() {
        return sharedFood;
    }

    public void setSharedFood(boolean sharedFood) {
        this.sharedFood = sharedFood;
    }

    public boolean isSharedXP() {
        return sharedXP;
    }

    public void setSharedXP(boolean sharedXP) {
        this.sharedXP = sharedXP;
    }

    public void addRequest(UUID uuid){
        if (this.requests.contains(uuid)) return;
        this.requests.add(uuid);
    }

    public void removeRequest(UUID uuid){
        this.requests.remove(uuid);
    }

    public void clearRequests(){
        this.requests.clear();
    }

    public List<UUID> getRequests() {
        return requests;
    }
}
