package com.fractured.cevents.pantheon;

import com.fractured.FracturedCore;
import com.fractured.user.PantheonMeta;
import com.fractured.user.User;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager
{
    private static final NamespacedKey DIALOGUE_HOLDER = FracturedCore.newNamespacedKey("dh");
    private static final Map<Integer, Response> rootDialogues = new HashMap<>();
    private static int id = Integer.MIN_VALUE;

    public static void newDialogueHolder(Location loc, String name, Response dialogue)
    {
        CraftVillager villager = (CraftVillager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);

        villager.setProfession(Villager.Profession.CARTOGRAPHER);

        villager.setGravity(false);
        villager.setNoPhysics(true);

        villager.getHandle().goalSelector.removeAllGoals(goal -> !(goal instanceof LookAtPlayerGoal));
        villager.getHandle().targetSelector.removeAllGoals(goal -> true);

        villager.setCustomName(name);
        rootDialogues.put(id, dialogue);
        villager.getPersistentDataContainer().set(DIALOGUE_HOLDER, PersistentDataType.INTEGER, id);

        ++id;
    }

    public static void onInteractAtEntity(User user, PlayerInteractAtEntityEvent event)
    {
        Entity entity = event.getRightClicked();

        Integer id = entity.getPersistentDataContainer().get(DIALOGUE_HOLDER, PersistentDataType.INTEGER);

        if (id != null)
        {
            // cancel for inventory opens or whatever else
            event.setCancelled(true);

            PantheonMeta meta = ((PantheonMeta) user.getEventMeta());

            // set a new dialogue holder
            Response response = rootDialogues.get(id);

            response.send(event.getPlayer());
            meta.setLastMessage(response);
        }
    }
}
