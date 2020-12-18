package me.mrfishcakes.propunish;

import de.leonhard.storage.Config;
import me.mrfishcakes.propunish.events.PunishEvents;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.storage.PunishmentComparator;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.LibraryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProPunishSpigot extends JavaPlugin implements ProPunishPlugin {

    private LibraryManager libraryManager;
    private Comparator<Punishment> comparator;
    private Config config;
    private PunishmentStorage storage;

    @Override
    public void onLoad() {
        libraryManager = new BukkitLibraryManager(this);
        loadCommonLibs();
    }

    @Override
    public void onEnable() {
        comparator = new PunishmentComparator();

        final Optional<Config> optionalConfig = setupConfig(getResource("config.yml"));
        if (!optionalConfig.isPresent()) return;

        config = optionalConfig.get();
        log(Level.INFO, "Config successfully loaded!");

        final Optional<PunishmentStorage> optionalStorage = setupStorage();
        if (!optionalStorage.isPresent()) {
            log(Level.SEVERE, "There was an error setting up the storage!");
            disablePlugin();
            return;
        }

        storage = optionalStorage.get();
        log(Level.INFO, "Storage method successfully setup!");

        Bukkit.getPluginManager().registerEvents(new PunishEvents(this), this);
    }

    @Override
    public void onDisable() {
        comparator = null;
        config = null;

        if (storage != null) storage.disable();
        storage = null;
    }

    @Override
    public void log(@NotNull Level level, @NotNull String message) {
        getLogger().log(level, message);
    }

    @Override
    public void log(@NotNull Level level, @NotNull String message, @NotNull Throwable throwable) {
        getLogger().log(level, message, throwable);
    }

    @Override
    public void disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public Comparator<Punishment> getPunishmentComparator() {
        return comparator;
    }

    @Override
    public Config getConfigYaml() {
        return config;
    }

    @Override
    public File getPluginFolder() {
        return getDataFolder();
    }

    @Override
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    @Override
    @Deprecated
    public Logger getLog() {
        return getLogger();
    }

    @Override
    public PunishmentStorage getStorage() {
        return storage;
    }

}
