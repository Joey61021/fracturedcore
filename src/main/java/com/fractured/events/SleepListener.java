    package com.fractured.events;

    import com.fractured.FracturedCore;
    import com.fractured.team.ClaimManager;
    import com.fractured.team.Team;
    import com.fractured.user.UserManager;
    import com.fractured.util.globals.Messages;
    import org.bukkit.Location;
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.Listener;
    import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        Location bedLoc = event.getBed().getLocation();
        if (bedLoc.getWorld() != WorldManager.getSpawn().getWorld()) {
            return;
        }

        Team team = UserManager.getUser(event.getPlayer()).getTeam();
        Team enemyTeam = ClaimManager.getClaim(bedLoc).getTeam();
        if (enemyTeam != null && enemyTeam != team) {
            event.getPlayer().sendMessage(FracturedCore.getMessages().get(Messages.CANNOT_SLEEP_HERE));
            event.setCancelled(true);
        }
    }
}
