    package com.fractured.events;

    import com.fractured.FracturedCore;
    import com.fractured.team.ClaimManager;
    import com.fractured.team.Team;
    import com.fractured.user.UserManager;
    import com.fractured.util.globals.Messages;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.World;
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.Listener;
    import org.bukkit.event.player.PlayerBedEnterEvent;
    import org.bukkit.plugin.java.JavaPlugin;

    public class SleepListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        Location bedLoc = event.getBed().getLocation();
        if (bedLoc.getWorld() != WorldManager.SPAWN.getWorld()) {
            return;
        }

        Team team = UserManager.getUser(event.getPlayer()).getTeam();
        Team enemyTeam = ClaimManager.getClaim(bedLoc).getTeam();
        if (enemyTeam != null && enemyTeam != team) {
            event.getPlayer().sendMessage(FracturedCore.getMessages().get(Messages.CANNOT_SLEEP_HERE));
            event.setCancelled(true);
            return;
        }

        if (!bedLoc.getWorld().isDayTime())
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(FracturedCore.class), () -> {
                World world = bedLoc.getWorld();
                world.setThundering(false);
                world.setStorm(false);
                world.setTime(0);
            }, 100L);
        }
    }
}
