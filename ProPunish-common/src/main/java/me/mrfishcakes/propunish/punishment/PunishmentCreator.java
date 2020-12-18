package me.mrfishcakes.propunish.punishment;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public final class PunishmentCreator {

    /**
     * Create a new {@link PunishmentCreator} instance.
     *
     * @param id     The internal ID of the punishment
     * @param target Target's {@link UUID}
     * @param issuer Issuer's {@link UUID}
     * @return A new PunishmentCreator instance
     */
    public static PunishmentCreator create(int id, UUID target, UUID issuer) {
        return new PunishmentCreator(id, target, issuer);
    }

    private final int id;
    private final UUID target;
    private final UUID issuer;
    private String targetName;
    private String issuerName;
    private long expiry;
    private String reason;
    private PunishmentType type;

    private PunishmentCreator(final int id, @NotNull final UUID target,
                              @NotNull final UUID issuer) {
        Preconditions.checkArgument(id > 0, "Invalid ID");
        Preconditions.checkNotNull(target, "Target cannot be null");
        Preconditions.checkNotNull(issuer, "Issuer cannot be null");

        this.id = id;
        this.target = target;
        this.issuer = issuer;
    }

    /**
     * Set the target's name.
     *
     * @param targetName Name of the target
     * @return Modified {@link PunishmentCreator}
     */
    public PunishmentCreator targetName(@NotNull final String targetName) {
        this.targetName = targetName;
        return this;
    }

    /**
     * Set the issuer's name.
     *
     * @param issuerName Name of the issuer
     * @return Modified {@link PunishmentCreator}
     */
    public PunishmentCreator issuerName(@NotNull final String issuerName) {
        this.issuerName = issuerName;
        return this;
    }

    /**
     * Set the duration.
     *
     * @param duration Duration of the punishment (-1 = permanent)
     * @return Modified {@link PunishmentCreator}
     */
    public PunishmentCreator duration(final long duration) {
        if (duration == -1L) {
            this.expiry = -1L;
            return this;
        }

        this.expiry = System.currentTimeMillis() + duration;
        return this;
    }

    /**
     * Set the reason.
     *
     * @param reason Reason for punishment
     * @return Modified {@link PunishmentCreator}
     */
    public PunishmentCreator reason(@NotNull final String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Set the {@link PunishmentType}
     *
     * @param type Type of punishment
     * @return Modified {@link PunishmentCreator}
     */
    public PunishmentCreator type(@NotNull final PunishmentType type) {
        this.type = type;
        return this;
    }

    /**
     * Transform the {@link PunishmentCreator} into a {@link Punishment}.
     *
     * @param storage {@link PunishmentStorage} used for storage
     * @return A new {@link Punishment}
     */
    public final Punishment get(@NotNull final PunishmentStorage storage) {
        return new Punishment(storage, id, target, targetName, issuer, issuerName, expiry,
                reason, type);
    }

}
