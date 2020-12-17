package me.mrfishcakes.propunish.types.collections;

import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class MuteCollection {

    private List<Punishment> mutes;

    /**
     * Initiate a new MuteCollection.
     *
     * @param target  Target to get punishments of
     * @param storage {@link PunishmentStorage} to use
     */
    public MuteCollection(@NotNull final UUID target, @NotNull final PunishmentStorage storage) {
        mutes = storage.getPunishmentsByType(target, PunishmentType.MUTE);
    }

    /**
     * Filter through punishments depending if they are active or not.
     *
     * @param active Check for active or inactive punishments
     * @return Modified {@link MuteCollection}
     */
    public MuteCollection active(final boolean active) {
        mutes = mutes.parallelStream().filter(mute -> mute.isActive() == active)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Filter through punishments by the issuer.
     *
     * @param issuer Issuer to filter by
     * @return Modified {@link MuteCollection}
     */
    public MuteCollection issuer(@NotNull final UUID issuer) {
        mutes = mutes.parallelStream().filter(mute -> mute.getIssuer().equals(issuer))
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Get an unmodifiable {@link List} containing all mutes.
     *
     * @return An unmodifiable {@link List} containing mutes
     * @see Collections#unmodifiableList(List)
     */
    public final List<Punishment> get() {
        return Collections.unmodifiableList(mutes);
    }

}
