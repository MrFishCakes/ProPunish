package me.mrfishcakes.propunish.storage.types.h2;

import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.PunishmentType;
import net.byteflux.libby.Library;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class H2PunishmentStorage extends PunishmentStorage {

    private static final Library H2_JDBC_DRIVER = Library.builder().groupId("com.h2database")
            .artifactId("h2").version("1.4.200").build();
    private static final Library HIKARI_CP = Library.builder().groupId("com.zaxxer")
            .artifactId("HikariCP").version("3.4.5").build();

    private File databaseFile;

    public H2PunishmentStorage(@NotNull ProPunishPlugin plugin) {
        super(plugin);

        plugin.getLibraryManager().addMavenCentral();
        plugin.getLibraryManager().downloadLibrary(H2_JDBC_DRIVER);
        plugin.getLibraryManager().downloadLibrary(HIKARI_CP);

        plugin.getLibraryManager().loadLibrary(H2_JDBC_DRIVER);
        plugin.getLibraryManager().loadLibrary(HIKARI_CP);

        databaseFile = new File(plugin.getPluginFolder(), "ProPunish.db");
        try {
            if (databaseFile.createNewFile()) {
                plugin.log(Level.INFO, "Created database file!");
            }
        } catch (IOException ex) {
            plugin.log(Level.SEVERE, "There was an error creating database", ex);
        }
    }

    @Override
    public void save(@NotNull Punishment punishment) {

    }

    @Override
    public void delete(@NotNull Punishment punishment) {

    }

    @Override
    public List<Punishment> getAllPunishments(@NotNull UUID target) {
        return null;
    }

    @Override
    public List<Punishment> getPunishmentsByType(@NotNull UUID target, @NotNull PunishmentType type) {
        return null;
    }

    @Override
    public void disable() {
        databaseFile = null;
    }

    public boolean setup() {
        return true;
    }

}
