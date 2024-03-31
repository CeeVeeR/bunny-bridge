package org.ceeveer.bunnyportalhop.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getLogger;

public class Bridge implements Listener {

    private final World worldBelow;
    private  final  int worldBelowThreshold;
    private final World worldAbove;
    private  final  int worldAboveThreshold;

    private  final  int teleportOffset;

    public Bridge(World worldBelow, int worldBelowThreshold, World worldAbove, int worldAboveThreshold, int teleportOffset) {
        this.worldBelow = worldBelow;
        this.worldBelowThreshold = worldBelowThreshold;
        this.worldAbove = worldAbove;
        this.worldAboveThreshold = worldAboveThreshold;
        this.teleportOffset = teleportOffset;

        getLogger().info("Created Bridge: " +
                "World Below: " + worldBelow.getName() + ", " +
                "Threshold Below: " + worldBelowThreshold + ", " +
                "World Above: " +  worldAbove.getName() + ", " +
                "Threshold Above: " + worldAboveThreshold + ", " +
                "Teleport Offset: " + teleportOffset);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double playerY = player.getLocation().getY();
        if (playerY > worldAboveThreshold + teleportOffset && playerY < worldBelowThreshold - teleportOffset)
            return;
        World currentWorld = player.getWorld();
        if (!isInSpecifiedWorlds(currentWorld))
            return;
        if (currentWorld.getName().equals(worldBelow.getName())) {
            teleportIfThresholdCrossed(player, currentWorld, worldBelowThreshold, worldAbove, worldAboveThreshold + teleportOffset);
        } else if (currentWorld.getName().equals(worldAbove.getName())) {
            teleportIfThresholdCrossed(player, currentWorld, worldAboveThreshold, worldBelow, worldAboveThreshold - teleportOffset);
        }
    }

    private void teleportIfThresholdCrossed(Player player, World currentWorld, int threshold, World targetWorld, int targetY) {
        if ((currentWorld.getName().equals(worldBelow.getName()) && player.getLocation().getY() > threshold) || (currentWorld
                .getName().equals(worldAbove.getName()) && player.getLocation().getY() < threshold))
            teleportPlayer(player, targetWorld, targetY);
    }

    private boolean isInSpecifiedWorlds(World world) {
        return (world.getName().equals("world") || world.getName().equals("world_the_sky"));
    }

    private void teleportPlayer(Player player, World destinationWorld, int destinationY) {

        if (destinationWorld != null) {
            Vector velocity = player.getVelocity();
            Location newLocation = new Location(destinationWorld, player.getLocation().getX(), destinationY, player.getLocation().getZ());
            player.teleport(newLocation);
            player.setVelocity(velocity);
        }
    }
}
