package me.mrfishcakes.propunish.plugin;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.storage.types.h2.H2PunishmentStorage;
import me.mrfishcakes.propunish.storage.types.json.JsonPunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ProPunishPlugin {

    /**
     * Loads SimplixStorage into the plugin.
     */
    default void loadCommonLibs() {
        LibraryManager libraryManager = getLibraryManager();
        libraryManager.addJitPack();

        Library simplixStorage = Library.builder().groupId("com.github.simplix-softworks")
                .artifactId("SimplixStorage").version("3.2.1").build();
        libraryManager.downloadLibrary(simplixStorage);
        libraryManager.loadLibrary(simplixStorage);
    }

    /**
     * Create and load the default {@link Config} file.
     *
     * @param input Config file as {@link InputStream}
     * @return {@link Optional} with/without the {@link Config}
     */
    default Optional<Config> setupConfig(final InputStream input) {
        if (input == null) {
            log(Level.SEVERE, "There was no 'config.yml' found",
                    new NullPointerException("No 'config.yml' found"));
            disablePlugin();
            return Optional.empty();
        }

        Config config = LightningBuilder.fromFile(new File(getPluginFolder(), "config.yml"))
                .addInputStream(input).setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.MANUALLY).createConfig();

        return Optional.of(config);
    }

    /**
     * Setup the {@link PunishmentStorage} and the appropriate adapter.
     *
     * @return {@link Optional} with/without the {@link PunishmentStorage}
     */
    default Optional<PunishmentStorage> setupStorage() {
        final Config config = getConfigYaml();

        switch (config.getOrDefault("Storage.Type", "h2").toLowerCase()) {
            case "json":
                return Optional.of(new JsonPunishmentStorage(this));
            case "h2":
            default:
                return Optional.of(new H2PunishmentStorage(this));
        }
    }

    /**
     * Call a punishment event.
     *
     * @param punishment {@link Punishment} for the event
     */
    void callPunishEvent(@NotNull final Punishment punishment);

    /**
     * Disable the plugin, unregistering all commands and listeners.
     */
    void disablePlugin();

    /**
     * Send a message to the {@link Logger}.
     *
     * @param level   {@link Level} to log at
     * @param message Message to log
     * @see Logger#log(Level, String)
     */
    void log(@NotNull final Level level, @NotNull final String message);

    /**
     * Send a message to the {@link Logger} with a {@link Throwable}.
     *
     * @param level     {@link Level} to log at
     * @param message   Message to log
     * @param throwable {@link Throwable} thrown
     * @see Logger#log(Level, String, Throwable)
     */
    void log(@NotNull final Level level, @NotNull final String message,
             @NotNull final Throwable throwable);

    /**
     * Get the {@link Comparator} for sorting punishments
     *
     * @return {@link Comparator} for sorting
     * @see java.util.Collections#sort(List, Comparator)
     */
    Comparator<Punishment> getPunishmentComparator();

    /**
     * Get the {@link Config}.
     *
     * @return Plugin config file
     */
    Config getConfigYaml();

    /**
     * Get the folder for the plugin.
     *
     * @return Plugin's folder
     */
    File getPluginFolder();

    /**
     * Get the {@link LibraryManager} for managing {@link net.byteflux.libby.Library Libraries}.
     *
     * @return Adapted {@link LibraryManager} for Bukkit/BungeeCord
     */
    LibraryManager getLibraryManager();

    /**
     * Get the {@link Logger} of the plugin.
     *
     * @return Plugin's logger
     * @see ProPunishPlugin#log(Level, String)
     * @see ProPunishPlugin#log(Level, String, Throwable)
     * @deprecated Now use 'log' methods
     */
    @Deprecated
    Logger getLog();

    /**
     * Get the {@link PunishmentStorage} with adapted type.
     *
     * @return Adapted storage type
     */
    PunishmentStorage getStorage();

}
