package me.danny.nativeplug;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class RustLoader extends JavaPlugin {

    @Override
    @SuppressWarnings("all")
    public void onLoad() {
        var nativeLib = new File(getDataFolder(), "rustbukkit.dll");
        if(!nativeLib.exists()) {
            nativeLib.getParentFile().mkdirs();
            getLogger().severe("Please copy the rustbukkit.dll into the plugin data folder.");
            setEnabled(false);
            return;
        }

        System.load(nativeLib.getAbsolutePath());
        Bukkit.getPluginManager().registerInterface(RustPluginLoader.class);
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
        } catch (IOException e) {
            getLogger().severe("Failed to list files for whatever reason:");
            e.printStackTrace();
            setEnabled(false);
            return;
        }
    }

}
