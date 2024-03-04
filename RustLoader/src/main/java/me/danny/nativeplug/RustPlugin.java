package me.danny.nativeplug;

import me.danny.nativeplug.jextract_gen.RustBukkit;
import me.danny.nativeplug.nativeapi.RustBukkitGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.*;

import java.io.*;
import java.lang.foreign.MemorySegment;
import java.util.List;
import java.util.logging.Logger;

public class RustPlugin implements Plugin {

    private final File lib;
    private final PluginLoader loader;
    private final NativePlugin plugin;
    private boolean enabled = false;

    private final MemorySegment bukkitImpl;


    public RustPlugin(File lib, RustPluginLoader loader, NativePlugin plugin) {
        this.lib = lib;
        this.loader = loader;
        this.plugin = plugin;

        bukkitImpl = RustBukkitGenerator.generate(plugin.arena());
        onLoad();
    }

    @Override
    public File getDataFolder() {
        return new File(lib.getParentFile(), "Data." + lib.getName());
    }

    @Override
    public PluginDescriptionFile getDescription() {
        try {
            return new PluginDescriptionFile(new StringReader("""
                    name: %s
                    main: Rust
                    version: unknown
                    author: unknown
                    """.formatted(getName())));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new PluginDescriptionFile(getName(), "unknown", "rust");
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return null;
    }

    @Override
    public InputStream getResource(String filename) {
        return null;
    }

    @Override
    public void saveConfig() {
    }

    @Override
    public void saveDefaultConfig() {
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
    }

    @Override
    public void reloadConfig() {
    }

    @Override
    public PluginLoader getPluginLoader() {
        return loader;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void onDisable() {
        if (enabled) {
            Bukkit.getLogger().info("Disabling '%s'...".formatted(getName()));
            plugin.onDisable(bukkitImpl);
            enabled = false;
            Bukkit.getLogger().info("'%s' has been disabled.".formatted(getName()));
        }
    }

    @Override
    public void onLoad() {
        plugin.onLoad(bukkitImpl);
    }

    @Override
    public void onEnable() {
        if (!enabled) {
            Bukkit.getLogger().info("Enabling '%s'...".formatted(getName()));
            plugin.onEnable(bukkitImpl);
            enabled = true;
            Bukkit.getLogger().info("'%s' has been enabled.".formatted(getName()));
        }
    }

    @Override
    public boolean isNaggable() {
        return false;
    }

    @Override
    public void setNaggable(boolean canNag) {
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return null;
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(String worldName, String id) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return Bukkit.getLogger();
    }

    @Override
    public String getName() {
        return lib.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
