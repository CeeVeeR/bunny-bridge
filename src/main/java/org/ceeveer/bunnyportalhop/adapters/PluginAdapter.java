package org.ceeveer.bunnyportalhop.adapters;

import org.bukkit.World;

public interface PluginAdapter {


    boolean enable();
    void initialize();

    boolean load(String worldName);

    void disable();

    World getWorldBelow();
    World getWorldAbove();
}
