package me.mrfishcakes.propunish.storage;

import com.google.common.base.Preconditions;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class PunishmentStorage {

    protected final ProPunishPlugin plugin;
    protected int lastId;

    /**
     * Initiate a new PunishmentStorage.
     *
     * @param plugin {@link ProPunishPlugin} with key methods/getters
     */
    public PunishmentStorage(@NotNull final ProPunishPlugin plugin) {
        Preconditions.checkNotNull(plugin, "ProPunishPlugin cannot be null");

        this.plugin = plugin;
    }

    /**
     * Save a {@link Punishment} to the chosen storage system.
     *
     * @param punishment Punishment to save
     */
    public abstract void save(@NotNull final Punishment punishment);

    /**
     * Delete a {@link Punishment} from the chosen storage system.
     *
     * @param punishment Punishment to delete
     */
    public abstract void delete(@NotNull final Punishment punishment);

    /**
     * Get all punishments for the target.
     *
     * @param target Target to retrieve punishments of
     * @return {@link List} containing all punishments
     */
    public abstract List<Punishment> getAllPunishments(@NotNull final UUID target);

    /**
     * Get all punishments for the target filtering by {@link PunishmentType}.
     *
     * @param target Target to retrieve punishments of
     * @param type   {@link PunishmentType} to filter by
     * @return {@link List} containing all punishments
     */
    public abstract List<Punishment> getPunishmentsByType(@NotNull final UUID target,
                                                          @NotNull final PunishmentType type);

    /**
     * Method called to 'clean' the class.
     */
    public abstract void disable();

    /**
     * Increase the last punishment ID as well as returning it.
     *
     * @return Increased ID (to use)
     */
    public final int increaseId() {
        lastId++;
        return lastId;
    }

}