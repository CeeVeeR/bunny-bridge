package org.ceeveer.bunnyportalhop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.ceeveer.bunnyportalhop.listeners.Bridge;
import org.bukkit.plugin.java.JavaPlugin;

public final class BunnyBridge extends JavaPlugin {

    private static final String PHANTOM_WORLDS_PLUGIN = "PhantomWorlds";
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Check for PhantomWorlds dependency

        Plugin pwpl = this.getServer().getPluginManager().getPlugin("PhantomWorlds");

        if (pwpl == null || !pwpl.isEnabled()) {
            getLogger().severe(PHANTOM_WORLDS_PLUGIN + " dependency not found. Disabling plugin.");
            disable();
            return;
        }

        getLogger().info(PHANTOM_WORLDS_PLUGIN + " dependency found!");


        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            configManager = new ConfigManager(this);
            WorldManager worldManager = new WorldManager(this);

            if (worldManager.attemptLoad(configManager.getWorldBelow()) && worldManager.attemptLoad(configManager.getWorldAbove())) {
                registerEventListeners();
            } else {
                getLogger().severe("Unexpected error occurred while initializing");
            }
        }, 20L);
    }

    private void registerEventListeners() {
        Bridge bridge = new Bridge(
                Bukkit.getWorld(configManager.getWorldBelow()),
                configManager.getWorldBelowThreshold(),
                Bukkit.getWorld(configManager.getWorldAbove()),
                configManager.getWorldAboveThreshold(),
                configManager.getTeleportOffset()

        );
        Bukkit.getPluginManager().registerEvents(bridge, this);
    }

    void disable() {
        getLogger().severe("Disabling " + getName());
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + getName());
    }
}
