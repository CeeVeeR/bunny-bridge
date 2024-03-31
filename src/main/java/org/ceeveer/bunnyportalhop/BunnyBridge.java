package org.ceeveer.bunnyportalhop;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.ceeveer.bunnyportalhop.adapters.AdapterManager;
import org.ceeveer.bunnyportalhop.adapters.PhantomWorldsAdapter;
import org.ceeveer.bunnyportalhop.adapters.PluginAdapter;
import org.ceeveer.bunnyportalhop.listeners.Bridge;
import org.ceeveer.bunnyportalhop.utils.ConfigManager;

public final class BunnyBridge extends JavaPlugin {

    private ConfigManager configManager;
    private AdapterManager adapterManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.adapterManager = new AdapterManager();

        PhantomWorldsAdapter pwAdapter = new PhantomWorldsAdapter(configManager, this);

        adapterManager.addAdapter(pwAdapter);
        adapterManager.initializeAdapters();

        if (adapterManager.isAnyAdapterEnabled()) {
            for (PluginAdapter adapter : adapterManager.getAdapters()) {
                World worldBelow = adapter.getWorldBelow();
                World worldAbove = adapter.getWorldAbove();

                if (worldBelow != null && worldAbove != null) {
                    registerEventListeners(worldBelow, worldAbove);
                    break;
                }
            }
        } else {
            getLogger().warning("No adapters enabled. Disabling plugin.");
            disable();
        }
    }

    private void registerEventListeners(World worldBelow, World worldAbove) {
        Bridge bridge = new Bridge(
                worldBelow,
                configManager.getWorldBelowThreshold(),
                worldAbove,
                configManager.getWorldAboveThreshold(),
                configManager.getTeleportOffset()
        );

        getServer().getPluginManager().registerEvents(bridge, this);
    }

    void disable() {
        getLogger().warning("Disabling " + getName());
        getServer().getPluginManager().disablePlugin(this);
        adapterManager.disableAdapters();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + getName());
    }
}
