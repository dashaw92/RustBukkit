package me.danny.nativeplug;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class RustPluginLoader implements PluginLoader {

    public RustPluginLoader(Server server) {}

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        if(!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        try {
            Bukkit.getLogger().info("Attempting to load '%s'".formatted(file.getAbsolutePath()));
            return new RustPlugin(file, this, JNIPlugin.open(file.getAbsolutePath()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidPluginException("Failed to load native plugin '%s'!".formatted(file.getName()));
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        return new PluginDescriptionFile(file.getName(), "unknown", "rust");
    }

    private static final Pattern[] EXTENSIONS = new Pattern[] { Pattern.compile("\\.dll$") };
    @Override
    public Pattern[] getPluginFileFilters() {
        return EXTENSIONS;
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
