package com.fractured.managers;

import com.fractured.FracturedCore;
import com.fractured.enums.Teams;
import com.fractured.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationManager {

    public static final int MAX_HEIGHT = 320;
    public static final int MIN_HEIGHT = -64;

    public static World world = Bukkit.getServer().getWorld("world");

    // Locations
    public static Location beaconLoc = new Location(world, 0, 63, 0);

    public static Location redPos1 = new Location(world, 750.5, MAX_HEIGHT, 750.5);
    public static Location redPos2 = new Location(world, 2.5, MIN_HEIGHT, 2.5);

    public static Location greenPos1 = new Location(world, -750.5, MAX_HEIGHT, -750.5);
    public static Location greenPos2 = new Location(world, -1.5, MIN_HEIGHT, -1.5);

    public static Location bluePos1 = new Location(world, 750.5, MAX_HEIGHT, -750.5);
    public static Location bluePos2 = new Location(world, 2.5, MIN_HEIGHT, 2.5);

    public static Location yellowPos1 = new Location(world, -750.5, MAX_HEIGHT, 750.5);
    public static Location yellowPos2 = new Location(world, 2.5, MIN_HEIGHT, 2.5);

    public static Location getLocation(String key) {
        Config settings = FracturedCore.getSettings;
        return new Location(world, settings.getDouble("locations." + key + ".x", 0.0), settings.getDouble("locations." + key + ".y", 64.0), settings.getDouble("locations." + key + ".z", 0.0), 0.0F, 0.0F);
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

    public static Teams getEnemyTeam(Teams team, Location loc) {
        for (Teams teams : Teams.values()) {
            if (teams != team && isInRegion(loc, teams.getPos1(), teams.getPos2())) {
                return teams;
            }
        }
        return null;
    }

}
