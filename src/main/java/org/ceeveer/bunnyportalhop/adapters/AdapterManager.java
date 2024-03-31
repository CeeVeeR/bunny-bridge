package org.ceeveer.bunnyportalhop.adapters;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class AdapterManager {
    private final List<PluginAdapter> adapters = new ArrayList<>();
    private boolean anyAdapterEnabled = false;

    public void addAdapter(PluginAdapter adapter) {
        adapters.add(adapter);
    }

    public void initializeAdapters() {
        for (PluginAdapter adapter : adapters) {
            if (adapter.enable()) {
                adapter.initialize();
                anyAdapterEnabled = true;
                Bukkit.getLogger().info(adapter.getClass().getSimpleName() + " loaded successfully.");
            }
        }
    }
    public boolean isAnyAdapterEnabled() {
        return anyAdapterEnabled;
    }

    public void disableAdapters() {
        adapters.forEach(PluginAdapter::disable);
    }

    public List<PluginAdapter> getAdapters() {
        return adapters;
    }

}