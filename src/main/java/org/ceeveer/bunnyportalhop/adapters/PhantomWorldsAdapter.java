package org.ceeveer.bunnyportalhop.adapters;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.WorldLoadResponse;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.ceeveer.bunnyportalhop.utils.ConfigManager;

import static org.bukkit.Bukkit.getLogger;

public class PhantomWorldsAdapter implements PluginAdapter {

    private final ConfigManager configManager;
    private final Plugin plugin;

    public PhantomWorldsAdapter(ConfigManager configManager, Plugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    private World worldBelow, worldAbove;

    @Override
    public boolean enable() {
        Plugin phantomWorldsPlugin = plugin.getServer().getPluginManager().getPlugin("PhantomWorlds");
        return phantomWorldsPlugin != null && phantomWorldsPlugin.isEnabled();

    }


    @Override
    public void initialize() {
        if (!enable()) {
            getLogger().severe("PhantomWorlds dependency not found or disabled.");
            return;
        }

        boolean isWorldBelowLoaded = load(configManager.getWorldBelow());
        boolean isWorldAboveLoaded = load(configManager.getWorldAbove());

        if (!isWorldBelowLoaded || !isWorldAboveLoaded) {
            disable();
        }
    }


    @Override
    public boolean load(String worldName) {
        getLogger().info("Attempting to load world: " + worldName);

        WorldLoadResponse loadResponse = PhantomWorlds.worldManager().loadWorld(worldName);
        switch (loadResponse) {
            case LOADED:
            case ALREADY_LOADED:
                getLogger().info("World '" + worldName + "' successfully loaded or was already loaded.");
                if(worldName.equals(configManager.getWorldBelow())) {
                    worldBelow = Bukkit.getWorld(worldName);
                } else if(worldName.equals(configManager.getWorldAbove())) {
                    worldAbove = Bukkit.getWorld(worldName);
                }
                return true;
            case NOT_FOUND:
            case INVALID:
                getLogger().warning("Failed to load world '" + worldName + "' due to it being not found or invalid.");
                disable();
                return false;
            case CONFIG_SKIPPED:
                getLogger().severe("World '" + worldName + "' is configured to be skipped by PhantomWorlds.");
                disable();
                return false;
            default:
                getLogger().severe("Unexpected error occurred while loading world '" + worldName + "'.");
                disable();
                return false;
        }

    }

    @Override
    public World getWorldBelow() {
        return worldBelow;
    }

    @Override
    public World getWorldAbove() {
        return worldAbove;
    }

    @Override
    public void disable() {
        // Disable logic if necessary
    }
}