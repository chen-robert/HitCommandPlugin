package me.crepppy.hitcommand;

import com.shampaggon.crackshot.CSUtility;
import me.crepppy.hitcommand.commands.NewWeapon;
import me.crepppy.hitcommand.listeners.PlayerHit;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class HitCommand extends JavaPlugin {
    private static HitCommand instance;
    private static CSUtility crackshot;

    public static HitCommand getInstance() {
        return instance;
    }

    public static CSUtility getCrackshot() {
        return crackshot;
    }

    @Override
    public void onEnable() {
        instance = this;
        crackshot = new CSUtility();
        getConfig().options().copyDefaults(true);
        saveConfig();
        Bukkit.getServer().getPluginCommand("hitcommand").setExecutor(new NewWeapon());
        Bukkit.getServer().getPluginManager().addPermission(new Permission("hitcommand.addweapon", "Allows the user to add a new weapon to the config in game.", PermissionDefault.OP));
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerHit(), this);
    }
}
