package com.fractured.commands;

import com.fractured.commands.subcommand.SubCommandRegistry;
import com.fractured.entity.DungeonBoss;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public final class TestCommand
{
    private static final SubCommandRegistry subcommands;

    static
    {
        subcommands = new SubCommandRegistry(0);

        subcommands.register("1", TestCommand::test1);
        subcommands.register("2", TestCommand::test2);
    }

    public static boolean test(CommandSender sender, Command command, String label, String[] args)
    {
        if (!sender.hasPermission("*"))
        {
            return true;
        }

        if (args.length < 1)
        {
            return true;
        }

        subcommands.dispatch(sender, command, label, args);

        return true;
    }

    private static boolean test1(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;

        ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(new DungeonBoss(player.getLocation()));
        return true;
    }

    private static boolean test2(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;
        ServerPlayer senderPlayer = ((CraftPlayer) sender).getHandle();

        MinecraftServer server = senderPlayer.getServer();
        ServerLevel level = senderPlayer.serverLevel();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "test2");
        ClientInformation info = new ClientInformation("en_us", 2, ChatVisiblity.HIDDEN, false, 0, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false, ParticleStatus.MINIMAL);

        ServerPlayer npc = new ServerPlayer(server, level, profile, info);
        npc.setPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());

        /* Retrieving skins:
         *  - First thing to understand is that skins have to be "signed" by Mojang. Each skin has two parts: texture and signature
         *  - You can retrieve this data from:
         *    https://sessionserver.mojang.com/session/minecraft/profile/UUID?unsigned=false
         *  - UUID = the uuid of a player without the dashes
         *  - UUID data can only be retrieved every 60 seconds (if same uuid)
         */
        String signature = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        npc.getGameProfile().getProperties().put("textures", new Property("textures", texture, signature));

        ServerEntity npcServerEntity = new ServerEntity(level, senderPlayer, 0, false, packet -> { }, Set.of()); // someone on a forum said to do that ...*1

        ServerGamePacketListenerImpl conn = senderPlayer.connection;

        npc.connection = new ServerGamePacketListenerImpl(server, new Connection(PacketFlow.CLIENTBOUND), npc, new CommonListenerCookie(profile, 0, info, false));

        // Tablist
        conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        // Summon entity
        conn.send(new ClientboundAddEntityPacket(npc, npcServerEntity));

        sender.sendMessage("spawned");

        return true;
    }
}
