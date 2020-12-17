package me.mrfishcakes.propunish.types.collections;

import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class BanCollection {

    private List<Punishment> bans;

    /**
     * Initiate a new BanCollection.
     *
     * @param target  Target to get punishments of
     * @param storage {@link PunishmentStorage} to use
     */
    public BanCollection(@NotNull final UUID target, @NotNull final PunishmentStorage storage) {
        bans = storage.getPunishmentsByType(target, PunishmentType.BAN);
    }

    /**
     * Filter through punishments depending if they are active or not.
     *
     * @param active Check for active or inactive punishments
     * @return Modified {@link BanCollection}
     */
    public BanCollection active(final boolean active) {
        bans = bans.parallelStream().filter(ban -> ban.isActive() == active)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Filter through punishments by the issuer.
     *
     * @param issuer Issuer to filter by
     * @return Modified {@link BanCollection}
     */
    public BanCollection issuer(@NotNull final UUID issuer) {
        bans = bans.parallelStream().filter(ban -> ban.getIssuer().equals(issuer))
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Get an unmodifiable {@link List} containing all bans.
     *
     * @return An unmodifiable {@link List} containing bans
     * @see Collections#unmodifiableList(List)
     */
    public final List<Punishment> get() {
        return Collections.unmodifiableList(bans);
    }

}
