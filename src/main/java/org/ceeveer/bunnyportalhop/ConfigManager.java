package org.ceeveer.bunnyportalhop;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final BunnyBridge plugin;
    private String worldBelow;
    private String worldAbove;
    private int worldBelowThreshold;
    private int worldAboveThreshold;
    private int teleportOffset;

    public ConfigManager(BunnyBridge plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        worldBelow = config.getString("WORLD_BELOW", "world");
        worldBelowThreshold = config.getInt("WORLD_BELOW_THRESHOLD", 333);
        worldAbove = config.getString("WORLD_ABOVE", "world_the_sky");
        worldAboveThreshold = config.getInt("WORLD_ABOVE_THRESHOLD", -30);
        teleportOffset = config.getInt("TELEPORT_OFFSET", 30);
    }

    public String getWorldBelow() {
        return worldBelow;
    }

    public String getWorldAbove() {
        return worldAbove;
    }

    public int getWorldBelowThreshold() {
        return worldBelowThreshold;
    }

    public int getWorldAboveThreshold() {
        return worldAboveThreshold;
    }

    public int getTeleportOffset() {
        return teleportOffset;
    }

}
