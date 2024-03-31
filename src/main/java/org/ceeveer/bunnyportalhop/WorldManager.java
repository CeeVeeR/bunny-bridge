package org.ceeveer.bunnyportalhop;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.WorldLoadResponse;

import static org.bukkit.Bukkit.getLogger;

public class WorldManager {
    private final BunnyBridge plugin;

    public WorldManager(BunnyBridge plugin) {
        this.plugin = plugin;
    }

    protected boolean attemptLoad(String worldName) {
        WorldLoadResponse loadResponse = PhantomWorlds.worldManager().loadWorld(worldName);
        switch (loadResponse) {
            case LOADED:
            case ALREADY_LOADED:
                getLogger().info("World '" + worldName + "' successfully loaded or was already loaded.");
                return true; // Success
            case NOT_FOUND:
            case INVALID:
                getLogger().warning("Failed to load world '" + worldName + "' due to it being not found or invalid.");
                plugin.disable();
                return false;
            case CONFIG_SKIPPED:
                getLogger().severe("World '" + worldName + "' is configured to be skipped by PhantomWorlds.");
                plugin.disable();
                return false;
            default:
                getLogger().severe("Unexpected error occurred while loading world '" + worldName + "'.");
                plugin.disable();
                return false;
        }
    }
}