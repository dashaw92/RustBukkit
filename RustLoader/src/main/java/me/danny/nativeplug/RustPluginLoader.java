package me.danny.nativeplug;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class RustPluginLoader implements PluginLoader {

    private static final Pattern[] EXTENSIONS = new Pattern[]{Pattern.compile("\\.dll$")};

    public RustPluginLoader(Server server) {
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(STR."\{file.getPath()} does not exist"));
        }

        try {
            Bukkit.getLogger().info("Attempting to load '%s'".formatted(file.getAbsolutePath()));
            return new RustPlugin(file, this, ForeignLoad.loadPlugin(file.getAbsoluteFile()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidPluginException("Failed to load native plugin '%s'!".formatted(file.getName()));
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        return new PluginDescriptionFile(new StringReader("""
                name: %s
                main: Rust
                version: unknown
                author: unknown
                """.formatted(file.getName())));
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return EXTENSIONS.clone();
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        return null;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        plugin.onEnable();
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        plugin.onDisable();
    }
}
