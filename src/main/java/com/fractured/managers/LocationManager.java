package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;

public class LocationManager {

    public static final int MAX_HEIGHT = 320;
    public static final int MIN_HEIGHT = -64;

    public static World world = Bukkit.getServer().getWorld("world");

    public static Location getLocation(String path) {
        Config settings = FracturedCore.getSettings;
        return new Location(world, settings.getDouble(path + ".x", 0.0), settings.getDouble(path + ".y", 64.0), settings.getDouble(path + ".z", 0.0), 0.0F, 0.0F);
    }

    public static boolean isInRegion(Location loc, Location pos1, Location pos2) {
        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        return pos1.getWorld().equals(loc.getWorld()) && loc.getX() > minX && loc.getX() < maxX && loc.getY() > minY && loc.getY() < maxY && loc.getZ() > minZ && loc.getZ() < maxZ;
    }

    public static Teams getEnemyTeam(Location loc, @Nullable Teams team) {
        for (Teams teams : Teams.values()) {
            if ((team == null || teams != team) && isInRegion(loc, teams.getPos1(), teams.getPos2())) {
                return teams;
            }
        }
        return null;
    }
}
