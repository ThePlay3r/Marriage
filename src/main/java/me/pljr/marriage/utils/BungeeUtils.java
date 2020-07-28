package me.pljr.marriage.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.pljr.marriage.Marriage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BungeeUtils {
    private static final Marriage instance = Marriage.getInstance();

    public static void message(String receiver, String message){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(receiver);
        out.writeUTF(message);

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) return;
        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }
}
