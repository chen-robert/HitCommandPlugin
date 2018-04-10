package me.crepppy.hitcommand.listeners;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import me.crepppy.hitcommand.HitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerHit implements Listener {
    @EventHandler
    public void onPlayerHitWithNormalWeapon(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        if (HitCommand.getCrackshot().getWeaponTitle(damager.getInventory().getItemInMainHand()) != null) return;
        for (String id : HitCommand.getInstance().getConfig().getKeys(false)) {
            if (!(e.getEntity() instanceof Player) && (HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").contains("%victim%")))
                continue;
            if (!HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("type").equalsIgnoreCase(damager.getInventory().getItemInMainHand().getType().toString()))
                continue;
            if (HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("displayname") && damager.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                if (!(ChatColor.translateAlternateColorCodes('&', HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("displayname")).equalsIgnoreCase(damager.getInventory().getItemInMainHand().getItemMeta().getDisplayName())))
                    continue;
            } else if (!(!HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("displayname") && !damager.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()))
                continue;
            if (HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("lore") && damager.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                List<String> configLore = HitCommand.getInstance().getConfig().getConfigurationSection(id).getStringList("lore");
                List<String> configLoreColored = new ArrayList<>();
                configLore.forEach((s) -> configLoreColored.add(ChatColor.translateAlternateColorCodes('&', s)));
                if (!configLoreColored.equals(damager.getInventory().getItemInMainHand().getItemMeta().getLore()))
                    continue;
            } else if (!(!HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("lore") && !damager.getInventory().getItemInMainHand().getItemMeta().hasLore()))
                continue;

            String command = HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").replaceAll("%attacker%", damager.getDisplayName());
            if (e.getEntity() instanceof Player)
                command.replaceAll("%victim%", ((Player) e.getEntity()).getDisplayName());
            executeCommandWithChance(command, HitCommand.getInstance().getConfig().getConfigurationSection(id).getInt("chance"), damager);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player damager = e.getEntity().getKiller();
        if (HitCommand.getCrackshot().getWeaponTitle(damager.getInventory().getItemInMainHand()) != null) return;
        for (String id : HitCommand.getInstance().getConfig().getKeys(false)) {
            if (!(e.getEntity() instanceof Player) && (HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").contains("%victim%")))
                continue;
            if (!HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("type").equalsIgnoreCase(damager.getInventory().getItemInMainHand().getType().toString()))
                continue;
            if (HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("displayname") && damager.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                if (!(ChatColor.translateAlternateColorCodes('&', HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("displayname")).equalsIgnoreCase(damager.getInventory().getItemInMainHand().getItemMeta().getDisplayName())))
                    continue;
            } else if (!(!HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("displayname") && !damager.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()))
                continue;
            if (HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("lore") && damager.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                List<String> configLore = HitCommand.getInstance().getConfig().getConfigurationSection(id).getStringList("lore");
                List<String> configLoreColored = new ArrayList<>();
                configLore.forEach((s) -> configLoreColored.add(ChatColor.translateAlternateColorCodes('&', s)));
                if (!configLoreColored.equals(damager.getInventory().getItemInMainHand().getItemMeta().getLore()))
                    continue;
            } else if (!(!HitCommand.getInstance().getConfig().getConfigurationSection(id).contains("lore") && !damager.getInventory().getItemInMainHand().getItemMeta().hasLore()))
                continue;
            String command = HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").replaceAll("%attacker%", damager.getDisplayName());
            if (e.getEntity() instanceof Player)
                command.replaceAll("%victim%", ((Player) e.getEntity()).getDisplayName());
            executeCommandWithChance(command, HitCommand.getInstance().getConfig().getConfigurationSection(id).getInt("deathChance"), damager);
        }
    }

    @EventHandler
    public void onHitWithCrackShotWeapon(WeaponDamageEntityEvent e) {
        for (String id : HitCommand.getInstance().getConfig().getKeys(false)) {
            if (!HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("type").equalsIgnoreCase(e.getWeaponTitle()))
                continue;
            if (!(e.getVictim() instanceof Player) && (HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").contains("%victim%")))
                continue;
            String command = HitCommand.getInstance().getConfig().getConfigurationSection(id).getString("command").replaceAll("%attacker%", e.getPlayer().getDisplayName());
            if (e.getVictim() instanceof Player)
                command.replaceAll("%victim%", ((Player) e.getVictim()).getDisplayName());
            executeCommandWithChance(command, HitCommand.getInstance().getConfig().getConfigurationSection(id).getInt(e.getVictim().isDead() ? "deathChance" : "chance"), e.getPlayer());
        }
    }

    private void executeCommandWithChance(String command, int chance, Player damager) {
        if (new Random().nextInt(100) <= chance) {
            if (command.startsWith("p:"))
                damager.performCommand(command.substring(2));
            else
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        }

    }
}
