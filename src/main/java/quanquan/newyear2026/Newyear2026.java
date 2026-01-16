package quanquan.newyear2026;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;

public final class Newyear2026 extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Newyear2026 plugin enabled!");
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Send New Year greeting message
        player.sendMessage(ChatColor.GOLD + "Welcome to the server!" + ChatColor.RED + " Happy New Year 2026!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        // Spawn fireworks and particles at the player's location
        Bukkit.getScheduler().runTask(this, () -> {
            spawnFirework(location);
            spawnParticles(location);
        });
    }

    private void spawnFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        // Generate random firework effects
        Random random = new Random();
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .withFade(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
                .with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)])
                .trail(random.nextBoolean())
                .flicker(random.nextBoolean())
                .build();

        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
    }

    private void spawnParticles(Location location) {
        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 50, 0.5, 0.5, 0.5, 0.05);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("makeDumpling")) {
            int count = dumplings.getOrDefault(player, 0) + 1;
            dumplings.put(player, count);
            return true;
        }

        if (label.equalsIgnoreCase("eatDumpling")) {
            int count = dumplings.getOrDefault(player, 0);
            if (count > 0) {
                dumplings.put(player, count - 1);
                player.setFoodLevel(Math.min(player.getFoodLevel() + 4, 20)); // Restore hunger
                player.sendMessage(ChatColor.YELLOW + "You ate a dumpling! Remaining dumplings: " + (count - 1));
            } else {
                player.sendMessage(ChatColor.RED + "You don't have any dumplings to eat!");
            }
            return true;
        }

        return false;
    }
}