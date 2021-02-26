package me.pljr.marriage.managers;

import me.pljr.marriage.config.Gender;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.database.DataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public class QueryManager {

    private final Plugin plugin;
    private final DataSource dataSource;

    public QueryManager(Plugin plugin, DataSource dataSource){
        this.plugin = plugin;
        this.dataSource = dataSource;

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS marriage_players (" +
                            "uuid char(36) NOT NULL PRIMARY KEY," +
                            "partnerID char(36)," +
                            "gender VARCHAR(255) NOT NULL," +
                            "pvp tinyint(1) NOT NULL," +
                            "sharedFood tinyint(1) NOT NULL," +
                            "sharedXP tinyint(1) NOT NULL," +
                            "lastSeen bigint(20) NOT NULL," +
                            "home_world varchar(255)," +
                            "home_x double," +
                            "home_y double," +
                            "home_z double," +
                            "home_yaw float," +
                            "home_pitch float);"
            );
            statement.executeUpdate();
            dataSource.close(connection, statement, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadPlayerAsync(UUID uuid, Consumer<MarriagePlayer> consumer){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> consumer.accept(loadPlayer(uuid)));
    }

    public MarriagePlayer loadPlayer(UUID uuid){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM marriage_players WHERE uuid='" + uuid + "'"
            );
            ResultSet results = statement.executeQuery();
            if (!results.next()){
                dataSource.close(connection, statement, null);
                return new MarriagePlayer(uuid);
            }

            Gender gender = Gender.valueOf(results.getString("gender"));
            UUID partnerID;
            if (results.getString("partnerID") == null){
                partnerID = null;
            }else{
                partnerID = UUID.fromString(results.getString("partnerID"));
            }
            boolean pvp = results.getBoolean("pvp");
            long lastSeen = results.getLong("lastSeen");
            Location home;
            String worldName = results.getString("home_world");
            if (worldName == null){
                home = null;
            }else{
                World world = Bukkit.getWorld(worldName);
                if (world == null) home = null;
                else {
                    home = new Location(
                            world,
                            results.getDouble("home_x"),
                            results.getDouble("home_y"),
                            results.getDouble("home_z"),
                            results.getFloat("home_yaw"),
                            results.getFloat("home_pitch")
                    );
                }
            }
            boolean sharedFood = results.getBoolean("sharedFood");
            boolean sharedXP = results.getBoolean("sharedXP");

            dataSource.close(connection, statement, results);
            return new MarriagePlayer(uuid, gender, partnerID, pvp, lastSeen, home, false, sharedFood, sharedXP);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new MarriagePlayer(uuid);
    }

    public void savePlayerAsync(MarriagePlayer player){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> savePlayer(player));
    }

    public void savePlayer(MarriagePlayer player){
        try {
            UUID uuid = player.getUniqueId();
            Gender gender = player.getGender();
            UUID partnerID = player.getPartnerID();
            boolean pvp = player.isPvp();
            long lastSeen = player.getLastSeen();
            Location home = player.getHome();
            String homeName = null;
            double homeX = 0;
            double homeY = 0;
            double homeZ = 0;
            float homeYaw = 0;
            float homePitch = 0;
            if (home != null){
                homeName = home.getWorld().getName();
                homeX = home.getX();
                homeY = home.getY();
                homeZ = home.getZ();
                homeYaw = home.getYaw();
                homePitch = home.getPitch();
            }
            boolean sharedFood = player.isSharedFood();
            boolean sharedXP = player.isSharedXP();

            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "REPLACE INTO marriage_players VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );
            statement.setString(1, uuid.toString());
            if (partnerID == null){
                statement.setString(2, null);
            }else{
                statement.setString(2, partnerID.toString());
            }
            statement.setString(3, gender.toString());
            statement.setBoolean(4, pvp);
            statement.setBoolean(5, sharedFood);
            statement.setBoolean(6, sharedXP);
            statement.setLong(7, lastSeen);
            statement.setString(8, homeName);
            statement.setDouble(9, homeX);
            statement.setDouble(10, homeY);
            statement.setDouble(11, homeZ);
            statement.setFloat(12, homeYaw);
            statement.setFloat(13, homePitch);
            statement.executeUpdate();
            dataSource.close(connection, statement, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
