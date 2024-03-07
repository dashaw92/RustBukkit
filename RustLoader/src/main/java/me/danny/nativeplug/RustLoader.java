package me.danny.nativeplug;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;

public final class RustLoader extends JavaPlugin {

    @Override
    @SuppressWarnings("all")
    public void onLoad() {
        try {
            var nativeLib = new File(getDataFolder(), "rustbukkit_sys.dll");
            System.load(nativeLib.getAbsolutePath());
        } catch(Exception ex) {
            getLogger().severe(STR."Failed to load rustbukkit_sys.dll from \{getDataFolder().getAbsolutePath()}. Please ensure the DLL exists there.");
            setEnabled(false);
            return;
        }
        Bukkit.getPluginManager().registerInterface(RustPluginLoader.class);
    }

    @Override
    public void onEnable() {
        try {
            Files.list(getDataFolder().getParentFile().toPath())
                    .filter(path -> path.toFile().isFile() && path.toFile().getName().endsWith(".dll"))
                    .forEach(path -> {
                        try {
                            Bukkit.getPluginManager().loadPlugin(path.toFile());
                        } catch (InvalidPluginException | InvalidDescriptionException e) {
                            e.printStackTrace();
                            getLogger().severe("Failed to load plugin '%s'".formatted(path.toFile().getAbsolutePath()));
                        }
                    });
        } catch (Exception ex) {
            getLogger().severe("Failed to list files for whatever reason:");
            setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        ForeignLoad.close();
    }
}
