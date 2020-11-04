package me.pljr.marriage.managers;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgDefaulthome;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.database.DataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class QueryManager {
    private final Marriage marriage = Marriage.getInstance();
    private final DataSource dataSource;

    public QueryManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void loadPlayerSync(UUID uuid){
        try {
            UUID partner;
            Gender gender;
            boolean pvp;
            boolean food;
            boolean xp;
            Location home;
            long lastseen;

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM marriage_players WHERE uuid=?"
            );
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String partnerUUID = resultSet.getString("partner");
                if (partnerUUID == null){
                    partner = null;
                }else{
                    partner = UUID.fromString(partnerUUID);
                }
                gender = Gender.valueOf(resultSet.getString("gender"));
                pvp = resultSet.getBoolean("pvp");
                food = resultSet.getBoolean("food");
                xp = resultSet.getBoolean("xp");
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
                pvp = CfgSettings.defaultPvP;
                food = CfgSettings.defaultFood;
                xp = CfgSettings.defaultXP;
                home = new Location(
                        Bukkit.getWorld(CfgDefaulthome.world),
                        CfgDefaulthome.x,
                        CfgDefaulthome.y,
                        CfgDefaulthome.z,
                        CfgDefaulthome.yaw,
                        CfgDefaulthome.pitch
                );
                lastseen = System.currentTimeMillis();
                gender = Gender.NONE;
            }
            CorePlayer corePlayer = new CorePlayer(gender, partner, pvp, lastseen, home, false, food, xp);
            Marriage.getPlayerManager().setCorePlayer(uuid, corePlayer);

            dataSource.close(connection, preparedStatement, resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadPlayer(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), ()->{
            try {
                UUID partner;
                Gender gender;
                boolean pvp;
                boolean food;
                boolean xp;
                Location home;
                long lastseen;

                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM marriage_players WHERE uuid=?"
                );
                preparedStatement.setString(1, uuid.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    String partnerUUID = resultSet.getString("partner");
                    if (partnerUUID == null){
                        partner = null;
                    }else{
                        partner = UUID.fromString(partnerUUID);
                    }
                    gender = Gender.valueOf(resultSet.getString("gender"));
                    pvp = resultSet.getBoolean("pvp");
                    food = resultSet.getBoolean("food");
                    xp = resultSet.getBoolean("xp");
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
                    pvp = CfgSettings.defaultPvP;
                    food = CfgSettings.defaultFood;
                    xp = CfgSettings.defaultXP;
                    home = new Location(
                            Bukkit.getWorld(CfgDefaulthome.world),
                            CfgDefaulthome.x,
                            CfgDefaulthome.y,
                            CfgDefaulthome.z,
                            CfgDefaulthome.yaw,
                            CfgDefaulthome.pitch
                    );
                    lastseen = System.currentTimeMillis();
                    gender = Gender.NONE;
                }
                CorePlayer corePlayer = new CorePlayer(gender, partner, pvp, lastseen, home, false, food, xp);
                Marriage.getPlayerManager().setCorePlayer(uuid, corePlayer);

                dataSource.close(connection, preparedStatement, resultSet);
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public void savePlayerSync(UUID uuid){
        try {
            CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(uuid);

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "REPLACE INTO marriage_players VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, uuid.toString());
            if (corePlayer.getPartner() == null){
                preparedStatement.setString(2, null);
            }else{
                preparedStatement.setString(2, corePlayer.getPartner().toString());
            }
            preparedStatement.setString(3, corePlayer.getGender().toString());
            preparedStatement.setBoolean(4, corePlayer.isPvp());
            preparedStatement.setBoolean(5, corePlayer.isFood());
            preparedStatement.setBoolean(6, corePlayer.isXp());
            preparedStatement.setLong(7, corePlayer.getLastseen());
            preparedStatement.setString(8, corePlayer.getHome().getWorld().getName());
            preparedStatement.setDouble(9, corePlayer.getHome().getX());
            preparedStatement.setDouble(10, corePlayer.getHome().getY());
            preparedStatement.setDouble(11, corePlayer.getHome().getZ());
            preparedStatement.setFloat(12, corePlayer.getHome().getYaw());
            preparedStatement.setFloat(13, corePlayer.getHome().getPitch());
            preparedStatement.executeUpdate();

            dataSource.close(connection, preparedStatement, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void savePlayer(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(marriage, () ->{
           try {
               CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(uuid);

               Connection connection = dataSource.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement(
                       "REPLACE INTO marriage_players VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
               );
               preparedStatement.setString(1, uuid.toString());
               if (corePlayer.getPartner() == null){
                   preparedStatement.setString(2, null);
               }else{
                   preparedStatement.setString(2, corePlayer.getPartner().toString());
               }
               preparedStatement.setString(3, corePlayer.getGender().toString());
               preparedStatement.setBoolean(4, corePlayer.isPvp());
               preparedStatement.setBoolean(5, corePlayer.isFood());
               preparedStatement.setBoolean(6, corePlayer.isXp());
               preparedStatement.setLong(7, corePlayer.getLastseen());
               preparedStatement.setString(8, corePlayer.getHome().getWorld().getName());
               preparedStatement.setDouble(9, corePlayer.getHome().getX());
               preparedStatement.setDouble(10, corePlayer.getHome().getY());
               preparedStatement.setDouble(11, corePlayer.getHome().getZ());
               preparedStatement.setFloat(12, corePlayer.getHome().getYaw());
               preparedStatement.setFloat(13, corePlayer.getHome().getPitch());
               preparedStatement.executeUpdate();

               dataSource.close(connection, preparedStatement, null);
           }catch (SQLException e){
               e.printStackTrace();
           }
        });
    }

    public LinkedHashMap<UUID, UUID> getMarryListSync(){
        LinkedHashMap<UUID, UUID> marryList = new LinkedHashMap<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM marriage_players"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            List<UUID> added = new ArrayList<>();
            while (resultSet.next()){
                if (added.contains(UUID.fromString(resultSet.getString("uuid")))) continue;
                if (resultSet.getString("partner") == null) continue;
                UUID player1id = UUID.fromString(resultSet.getString("uuid"));
                UUID player2id = UUID.fromString(resultSet.getString("partner"));
                marryList.put(player1id, player2id);
                added.add(player1id);
                added.add(player2id);
            }
            dataSource.close(connection, preparedStatement, resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return marryList;
    }

    public void setupTables() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS marriage_players (" +
                            "uuid char(36) NOT NULL PRIMARY KEY," +
                            "partner char(36)," +
                            "gender varchar(255) NOT NULL," +
                            "pvp tinyint(1) NOT NULL," +
                            "food tinyint(1) NOT NULL," +
                            "xp tinyint(1) NOT NULL," +
                            "lastseen bigint(20) NOT NULL," +
                            "home_world varchar(255) NOT NULL," +
                            "home_x double NOT NULL," +
                            "home_y double NOT NULL," +
                            "home_z double NOT NULL," +
                            "home_yaw float NOT NULL," +
                            "home_pitch float NOT NULL);"
            );
            preparedStatement.executeUpdate();
            dataSource.close(connection, preparedStatement, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
