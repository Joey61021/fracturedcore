package com.fractured.cevents.pantheon;

import com.fractured.FracturedCore;
import com.fractured.user.PantheonMeta;
import com.fractured.user.User;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);

        villager.setProfession(Villager.Profession.CARTOGRAPHER);

        ((net.minecraft.world.entity.npc.Villager) villager).goalSelector.removeAllGoals(goal -> true);
        ((net.minecraft.world.entity.npc.Villager) villager).targetSelector.removeAllGoals(goal -> true);

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
            PantheonMeta meta = ((PantheonMeta) user.getEventMeta());

            if (meta.getDialogueHolder() != null && meta.getDialogueHolder().equals(id))
            {
                event.getPlayer().sendMessage("talked to the same entity");
                // If the user is talking to the same entity, we need to progress their dialogue
            } else
            {
                // set a new dialogue holder
                event.getPlayer().sendMessage("talked to a different entity");

                Response response = rootDialogues.get(id);

                response.send(event.getPlayer());
                meta.startConversation(id, response);
            }
        }
    }
}
