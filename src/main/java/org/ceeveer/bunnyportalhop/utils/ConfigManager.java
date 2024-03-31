package org.ceeveer.bunnyportalhop.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        validateConfig();
    }

    private void validateConfig() {
        boolean configValid = true;

        if (getString("WORLD_BELOW") == null || Bukkit.getWorld(getString("WORLD_BELOW")) == null) {
            plugin.getLogger().warning("WORLD_BELOW configuration is invalid or refers to a non-existent world.");
            configValid = false;
        }

        if (getString("WORLD_ABOVE") == null || Bukkit.getWorld(getString("WORLD_ABOVE")) == null) {
            plugin.getLogger().warning("WORLD_ABOVE configuration is invalid or refers to a non-existent world.");
            configValid = false;
        }

        if (getInt("WORLD_BELOW_THRESHOLD", Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            plugin.getLogger().warning("WORLD_BELOW_THRESHOLD configuration is invalid.");
            configValid = false;
        }

        if (getInt("WORLD_ABOVE_THRESHOLD", Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            plugin.getLogger().warning("WORLD_ABOVE_THRESHOLD configuration is invalid.");
            configValid = false;
        }

        if (getInt("TELEPORT_OFFSET", Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            plugin.getLogger().warning("TELEPORT_OFFSET configuration is invalid.");
            configValid = false;
        }

        if (!configValid) {
            plugin.getLogger().warning("Invalid configuration. Plugin will be disabled.");
            disablePlugin();
        }
    }

    public String getString(String path) {
        return config.getString(path, null);
    }

    public int getInt(String path, int defaultValue) {
        return config.getInt(path, defaultValue);
    }

    private void disablePlugin() {
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    public String getWorldBelow() {
        return getString("WORLD_BELOW");
    }

    public String getWorldAbove() {
        return getString("WORLD_ABOVE");
    }

    public int getWorldBelowThreshold() {
        return getInt("WORLD_BELOW_THRESHOLD", 333);
    }

    public int getWorldAboveThreshold() {
        return getInt("WORLD_ABOVE_THRESHOLD", -30);
    }

    public int getTeleportOffset() {
        return getInt("TELEPORT_OFFSET", 30);
    }
}
