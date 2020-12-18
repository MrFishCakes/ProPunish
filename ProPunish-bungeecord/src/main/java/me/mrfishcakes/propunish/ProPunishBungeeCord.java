package me.mrfishcakes.propunish;

import de.leonhard.storage.Config;
import me.mrfishcakes.propunish.event.BungeePunishEvent;
import me.mrfishcakes.propunish.events.PunishEvents;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.storage.PunishmentComparator;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import net.byteflux.libby.BungeeLibraryManager;
import net.byteflux.libby.LibraryManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProPunishBungeeCord extends Plugin implements ProPunishPlugin {

    private LibraryManager libraryManager;
    private Comparator<Punishment> comparator;
    private Config config;
    private PunishmentStorage storage;

    @Override
    public void onLoad() {
        libraryManager = new BungeeLibraryManager(this);
        loadCommonLibs();
    }

    @Override
    public void onEnable() {
        comparator = new PunishmentComparator();

        final Optional<Config> optionalConfig = setupConfig(getResourceAsStream("config.yml"));
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

        getProxy().getPluginManager().registerListener(this, new PunishEvents(this));
    }

    @Override
    public void onDisable() {
        comparator = null;
        config = null;

        if (storage != null) storage.disable();
        storage = null;
    }

    @Override
    public void callPunishEvent(@NotNull Punishment punishment) {
        getProxy().getPluginManager().callEvent(new BungeePunishEvent(punishment));
    }

    @Override
    public void disablePlugin() {
        getProxy().getPluginManager().unregisterCommands(this);
        getProxy().getPluginManager().unregisterListeners(this);
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
