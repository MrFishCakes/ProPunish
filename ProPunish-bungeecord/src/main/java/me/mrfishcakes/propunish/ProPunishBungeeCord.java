package me.mrfishcakes.propunish;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import me.mrfishcakes.propunish.events.PunishEvents;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.storage.PunishmentComparator;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.storage.types.h2.H2PunishmentStorage;
import me.mrfishcakes.propunish.storage.types.json.JsonPunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import net.byteflux.libby.BungeeLibraryManager;
import net.byteflux.libby.LibraryManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
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

        if (!setupConfig()) {
            getProxy().getPluginManager().unregisterListeners(this);
            getProxy().getPluginManager().unregisterCommands(this);
            return;
        }
        getLogger().info("Config successfully loaded!");

        if (!setupStorage()) {
            getProxy().getPluginManager().unregisterListeners(this);
            getProxy().getPluginManager().unregisterCommands(this);
            return;
        }
        getLogger().info("Storage successfully setup!");

        getProxy().getPluginManager().registerListener(this, new PunishEvents(this));
    }

    @Override
    public void onDisable() {
        comparator = null;
        config = null;

        if (storage != null) storage.disable();
        storage = null;
    }

    private boolean setupConfig() {
        try (InputStream inputStream = getResourceAsStream("config.yml")) {
            if (inputStream == null) throw new NullPointerException("No 'config.yml' found");

            config = LightningBuilder.fromFile(new File(getPluginFolder(), "config.yml"))
                    .addInputStream(inputStream).setReloadSettings(ReloadSettings.INTELLIGENT)
                    .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                    .setDataType(DataType.SORTED).createConfig();

            return true;
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "There was an error with the config", ex);
            return false;
        }
    }

    private boolean setupStorage() {
        String type = config.getOrDefault("Storage.Type", "H2");
        switch (type.toLowerCase()) {
            case "json":
                storage = new JsonPunishmentStorage(this);
                return true;
            case "h2":
            default:
                storage = new H2PunishmentStorage(this);
                return ((H2PunishmentStorage) storage).setup();
        }
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
    public Logger getLog() {
        return getLogger();
    }

    @Override
    public PunishmentStorage getStorage() {
        return storage;
    }

}
