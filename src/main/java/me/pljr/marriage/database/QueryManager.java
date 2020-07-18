package me.pljr.marriage.database;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.managers.ConfigManager;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QueryManager {
    private final Marriage marriage = Marriage.getInstance();
    private final ConfigManager config = Marriage.getConfigManager();

    public void loadPlayerSync(String username){
        try {
            String partner;
            Gender gender;
            boolean pvp;
            Location home;
            long lastseen;

            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM marriage_players WHERE username=?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                partner = resultSet.getString("partner");
                gender = Gender.valueOf(resultSet.getString("gender"));
                pvp = resultSet.getBoolean("pvp");
                lastseen = resultSet.getLong("lastseen");
                home = new Location(
                        Bukkit.getWorld(resultSet.getString("home_world")),
                        resultSet.getDouble("home_x"),
                        resultSet.getDouble("home_y"),
                        resultSet.getDouble("home_z"),
                        resultSet.getFloat("home_yaw"),
                        resultSet.getFloat("home_pitch"));
            }else{
                partner = null;
                pvp = false;
                home = new Location(
                        Bukkit.getWorld(config.getString("defaulthome.world")),
                        config.getInt("defaulthome.x"),
                        config.getInt("defaulthome.y"),
                        config.getInt("defaulthome.z"),
                        config.getInt("defaulthome.yaw"),
                        config.getInt("defaulthome.pitch")
                );
                lastseen = System.currentTimeMillis();
                gender = Gender.NONE;
            }
            PlayerManager playerManager = new PlayerManager(gender, partner, pvp, lastseen, home);
            PlayerUtil.setPlayerManager(username, playerManager);

            DataSource.close(connection, preparedStatement, resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void savePlayer(String username){
        Bukkit.getScheduler().runTaskAsynchronously(marriage, () ->{
           try {
               PlayerManager playerManager = PlayerUtil.getPlayerManager(username);

               Connection connection = DataSource.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement(
                       "REPLACE INTO marriage_players VALUES (?,?,?,?,?,?,?,?,?,?,?)"
               );
               preparedStatement.setString(1, username);
               preparedStatement.setString(2, playerManager.getPartner());
               preparedStatement.setString(3, playerManager.getGender().toString());
               preparedStatement.setBoolean(4, playerManager.isPvp());
               preparedStatement.setLong(5, playerManager.getLastseen());
               preparedStatement.setString(6, playerManager.getHome().getWorld().getName());
               preparedStatement.setDouble(7, playerManager.getHome().getX());
               preparedStatement.setDouble(8, playerManager.getHome().getY());
               preparedStatement.setDouble(9, playerManager.getHome().getZ());
               preparedStatement.setFloat(10, playerManager.getHome().getYaw());
               preparedStatement.setFloat(11, playerManager.getHome().getPitch());
               preparedStatement.executeUpdate();

               DataSource.close(connection, preparedStatement, null);
           }catch (SQLException e){
               e.printStackTrace();
           }
        });
    }

    public LinkedHashMap<String, String> getMarryListSync(){
        LinkedHashMap<String, String> marryList = new LinkedHashMap<>();
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM marriage_players"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> added = new ArrayList<>();
            while (resultSet.next()){
                if (added.contains(resultSet.getString("username"))) continue;
                String player1 = resultSet.getString("username");
                String player2 = resultSet.getString("partner");
                if (player2 == null) continue;
                marryList.put(player1, player2);
                added.add(player1);
                added.add(player2);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return marryList;
    }

    public void setupTables() {
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS marriage_players (" +
                            "username varchar(16) NOT NULL PRIMARY KEY," +
                            "partner varchar(16)," +
                            "gender varchar(255) NOT NULL," +
                            "pvp tinyint(1) NOT NULL," +
                            "lastseen bigint(20) NOT NULL," +
                            "home_world varchar(255) NOT NULL," +
                            "home_x double NOT NULL," +
                            "home_y double NOT NULL," +
                            "home_z double NOT NULL," +
                            "home_yaw float NOT NULL," +
                            "home_pitch float NOT NULL);"
            );
            preparedStatement.executeUpdate();
            DataSource.close(connection, preparedStatement, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
