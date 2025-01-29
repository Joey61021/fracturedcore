
package com.fractured.events;

import com.fractured.FracturedCore;
import com.fractured.team.Team;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.globals.ConfigKeys;
import com.fractured.util.globals.Messages;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        // if friendly fire is off (we should cancel the event if the same team is attacking itself)
        if (!FracturedCore.getFracturedConfig().get(ConfigKeys.FRIENDLY_FIRE))
        {
            User damagerUser = UserManager.getUser(damager.getUniqueId());

            // Because these are being taken from an entity, we have to check if it's null or not. The entity is a player if there is a user corresponding to that uuid
            if (damagerUser == null)
            {
                return;
            }

            User victimUser = UserManager.getUser(victim.getUniqueId());

            if (victimUser == null)
            {
                return;
            }
            Team damagerTeam = damagerUser.getTeam();

            if (victimUser.getTeam() == damagerTeam)
            {
                event.setCancelled(true);
                damager.sendMessage(FracturedCore.getMessages().get(Messages.FRIENDLY_FIRE_DISABLED));
            }
        }
    }
}
