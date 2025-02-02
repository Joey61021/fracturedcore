package com.fractured.team;

import com.fractured.FracturedCore;
import com.fractured.menu.Menu;
import com.fractured.menu.MenuManager;
import com.fractured.team.upgrades.Upgrades;
import com.fractured.team.upgrades.UpgradeRequisite;
import com.fractured.user.User;
import com.fractured.user.UserManager;
import com.fractured.util.Utils;
import com.fractured.util.globals.Messages;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team
{
    /**
     * As in the database
     */
    private final int teamId;
    private int totalMembers;
    private final String name;
    private final ChatColor color;
    private final Material beacon;
    private final Location spawn;
    private final List<Player> onlineTeamMembers;
    private ItemStack helmet;
    private final Map<Integer, UpgradeRequisite> upgrades;
    private final Inventory upgradesMenu;

    private static final String TEAM_UPGRADES = "Team Upgrades";

    static
    {
        Menu teamUpgrades = new Menu();
        teamUpgrades.register(9 + 4, event ->
        {
            HumanEntity player = event.getView().getPlayer();
            User user = UserManager.getUser(player);
            Team team = user.getTeam();

            if (team == null)
            {
                player.sendMessage("You are not in a team!");
                player.closeInventory();
            } else
            {
                UpgradeRequisite requisite = team.upgrades.get(Upgrades.HELMET_ID);

                Material costType = requisite.material();
                int cost = requisite.cost();

                player.sendMessage("TODO");
            }

            // upgrades
        });

        MenuManager.register(TEAM_UPGRADES, teamUpgrades);
    }

    private static final Map<Character, Color> colorMap = Map.of(
            'c', Color.RED,
            '9', Color.BLUE,
            'a', Color.GREEN
    );

    public Team(int teamId, int totalMembers, String name, char color, Location spawn)
    {
        // Stuff from the database
        this.teamId = teamId;
        this.totalMembers = totalMembers;
        this.name = name;
        this.color = ChatColor.getByChar(color);
        this.spawn = spawn;

        beacon = Utils.getGlassFrom(color);

        onlineTeamMembers = new ArrayList<>();
        upgrades = new HashMap<>();
        upgradesMenu = Bukkit.createInventory(null, 3 * 9, TEAM_UPGRADES);

        helmet = new ItemStack(Material.LEATHER_HELMET);

        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();

        meta.setColor(colorMap.getOrDefault(color, Color.YELLOW));

        meta.addEnchant(Enchantment.PROTECTION, 5, true);
        meta.setUnbreakable(true);
        meta.setDisplayName(this.color + this.name + " team");

        helmet.setItemMeta(meta);
    }

    public void upgrade(Upgrades upgrade)
    {
        // When an upgrade is submitted to a team,
        // * set the level in the team upgrade's map
        // * call the upgrade callback
        // * update how to get the cost
        //upgrades.get
    }

    private void setPlayer(Player player)
    {
        onlineTeamMembers.add(player);
        // Display name
        player.setDisplayName(color() + player.getName());

        // Tab List
        player.setPlayerListName(player.getDisplayName());
        player.setPlayerListFooter(FracturedCore.getMessages().get(Messages.TAB_FOOTER_TEAM).replace("%color%", color.toString()).replace("%team%", name));
    }

    private void clearPlayer(Player player)
    {
        onlineTeamMembers.remove(player);
        player.setDisplayName(ChatColor.GRAY + player.getName());

        player.setPlayerListName(player.getDisplayName());
        player.setPlayerListFooter(FracturedCore.getMessages().get(Messages.TAB_FOOTER_NO_TEAM));
    }

    /**
     * Someone was added to the team
     */
    public void addMember(Player player)
    {
        ++totalMembers; // fixme save at shutdown in the database
        player.getInventory().setHelmet(helmet);
        setPlayer(player);
    }

    /**
     * Someone on the team joined the server
     */
    public void memberJoined(Player player)
    {
        setPlayer(player);
        // alert()
    }

    /**
     * Someone was removed from the team
     */
    public void removeMember(Player player)
    {
        --totalMembers;
        clearPlayer(player);
        player.getInventory().setHelmet(null);
    }

    /**
     * Someone on the team left the server
     */
    public void memberQuit(Player player)
    {
        clearPlayer(player);
    }

    public void alert(String message)
    {
        for (Player player : onlineTeamMembers)
        {
            player.sendMessage(message);
        }
    }

    public boolean isOffline()
    {
        return onlineTeamMembers.isEmpty();
    }

    /**
     * Total members, online or offline.
     */
    public int getTotalMembers()
    {
        return totalMembers;
    }

    public int getId()
    {
        return teamId;
    }

    public String getName()
    {
        return name;
    }

    public ChatColor color()
    {
        return color;
    }

    /**
     * @return Beacon material
     */
    public Material beacon()
    {
        return beacon;
    }

    public List<Player> getOnlineMembers()
    {
        return onlineTeamMembers;
    }

    public Location spawn()
    {
        return spawn;
    }

    public ItemStack helmet()
    {
        return helmet;
    }

    public Inventory getUpgradesMenu()
    {
        return upgradesMenu;
    }
}
