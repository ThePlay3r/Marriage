package me.pljr.marriage.objects;

import lombok.Getter;
import lombok.Setter;
import me.pljr.marriage.config.Gender;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter @Getter
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
}
