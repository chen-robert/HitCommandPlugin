package me.crepppy.hitcommand.commands;

import me.crepppy.hitcommand.HitCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NewWeapon implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cms, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }
        if (!sender.hasPermission("hitcommand.addweapon")) {
            sender.sendMessage(ChatColor.RED + "You have insufficient permissions to run this command.");
            return true;
        }
        Player p = (Player) sender;
        String usage = "\n" + ChatColor.RED + "/hitcommand <Command> <HitChance> <DeathChance>";
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Please specify the command to run when the player gets hit with this item." + usage);
        }
        if (args.length == 1 || args.length == 2) {
            p.sendMessage(ChatColor.RED + "Please specify the hit and death chance of the command executing when the player is hit with this item." + usage);
        }
        if (args.length >= 3) {
            int hit, death;
            try {
                hit = Integer.parseInt(args[args.length - 2]);
                death = Integer.parseInt(args[args.length - 1]);
            } catch (Exception e) {
                p.sendMessage(ChatColor.RED + "Please specify the hit and death chance of the command executing when the player is hit with this item." + usage);
                return true;
            }
            int section = 1;
            if (!HitCommand.getInstance().getConfig().getKeys(false).isEmpty())
                section = (Integer.parseInt((String) HitCommand.getInstance().getConfig().getKeys(false).toArray()[HitCommand.getInstance().getConfig().getKeys(false).size() - 1]) + 1);
            if (HitCommand.getCrackshot().getWeaponTitle(p.getInventory().getItemInMainHand()) == null) {
                HitCommand.getInstance().getConfig().set(section + ".type", p.getInventory().getItemInMainHand().getType().toString());
                if (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                    HitCommand.getInstance().getConfig().set(section + ".displayname", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    HitCommand.getInstance().getConfig().set(section + ".lore", p.getInventory().getItemInMainHand().getItemMeta().getLore());
                }
            } else
                HitCommand.getInstance().getConfig().set(section + ".type", HitCommand.getCrackshot().getWeaponTitle(p.getInventory().getItemInMainHand()));

            String command = "";
            for (String s : Arrays.copyOfRange(args, 0, args.length - 2)) command += " " + s;
            HitCommand.getInstance().getConfig().set(section + ".command", command.trim());
            HitCommand.getInstance().getConfig().set(section + ".chance", hit);
            HitCommand.getInstance().getConfig().set(section + ".deathChance", death);
            HitCommand.getInstance().saveConfig();
            p.sendMessage(ChatColor.DARK_AQUA + "HitCommand » " + ChatColor.GRAY + "Successfully added this weapon to config.");
        }
        return true;
    }
}
