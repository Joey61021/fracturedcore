package com.fractured.commands;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
        subcommands.register("3", TestCommand::test3);
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

        // npc skin
        PlayerProfile playerProfile = Bukkit.createProfile("Texxyy");

        // make sure the skin was queried from the api
        if (!playerProfile.isComplete())
        {
            playerProfile.complete(true);
        }

        npc.getGameProfile().getProperties().put("textures", (Property) ((CraftPlayerProfile) playerProfile).getGameProfile().getProperties().get("textures").toArray()[0]);

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

    private static boolean test3(CommandSender sender, Command command, String label, String[] args)
    {



        return true;
    }
}
