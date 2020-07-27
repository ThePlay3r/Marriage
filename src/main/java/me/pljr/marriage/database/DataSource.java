package me.pljr.marriage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.pljr.marriage.Marriage;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {

    private String host, port, database, username, password;
    private FileConfiguration configuration;

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public void load() {
        configuration = Marriage.getConf();
        host = configuration.getString("mysql.host");
        port = configuration.getString("mysql.port");
        database = configuration.getString("mysql.database");
        username = configuration.getString("mysql.username");
        password = configuration.getString("mysql.password");
    }

    public void initPool() {
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + Integer.parseInt(port) + "/" + database + "?characterEncoding=UTF-8&autoReconnect=true&useSSL=false");
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(16);
        ds = new HikariDataSource(config);
    }

    public DataSource() {}

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
        }
        if (ps != null) try {
            ps.close();
        } catch (SQLException ignored) {
        }
        if (res != null) try {
            res.close();
        } catch (SQLException ignored) {
        }
    }
}
