package me.mrfishcakes.propunish.punishment.collections;

import me.mrfishcakes.propunish.punishment.Punishment;
import me.mrfishcakes.propunish.punishment.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishmentCollection {

    private List<Punishment> allPunishments;

    /**
     * Initiate a new PunishmentCollection.
     *
     * @param punishments Previous punishments
     */
    public PunishmentCollection(List<Punishment> punishments) {
        if (punishments == null) {
            punishments = Collections.emptyList();
        }
        this.allPunishments = punishments;
    }

    /**
     * Filter through punishments depending if they are active or not.
     *
     * @param active Check for active or inactive punishments
     * @return Modified {@link PunishmentCollection}
     */
    public PunishmentCollection active(final boolean active) {
        allPunishments = allPunishments.parallelStream().filter(punishment -> punishment.isActive()
                == active).collect(Collectors.toList());
        return this;
    }

    /**
     * Filter through punishments by their type.
     *
     * @param type {@link PunishmentType} to filter by
     * @return Modified {@link PunishmentCollection}
     */
    public PunishmentCollection type(@NotNull final PunishmentType type) {
        allPunishments = allPunishments.parallelStream().filter(punishment -> punishment.getType()
                == type).collect(Collectors.toList());
        return this;
    }

    /**
     * Filter through punishments by the issuer.
     *
     * @param issuer Issuer to filter by
     * @return Modified {@link PunishmentCollection}
     */
    public PunishmentCollection issuer(@NotNull final UUID issuer) {
        allPunishments = allPunishments.parallelStream().filter(punishment ->
                punishment.getIssuer().equals(issuer)).collect(Collectors.toList());
        return this;
    }

    /**
     * Get an unmodifiable {@link List} containing all punishments.
     *
     * @return An unmodifiable {@link List} containing punishments
     * @see Collections#unmodifiableList(List)
     */
    public final List<Punishment> get() {
        return Collections.unmodifiableList(allPunishments);
    }
}
