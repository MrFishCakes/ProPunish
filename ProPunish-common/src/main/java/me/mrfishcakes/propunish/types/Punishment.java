package me.mrfishcakes.propunish.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.mrfishcakes.propunish.storage.PunishmentStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Punishment {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm");

    private final PunishmentStorage storage;
    private final int id;
    private final UUID target;
    private final String targetName;
    private final UUID issuer;
    private final String issuerName;
    private final long expiry;
    private final String reason;
    private final PunishmentType type;

    /**
     * Check if the punishment is permanent.
     *
     * @return True if permanent
     */
    public boolean isPermanent() {
        return expiry == -1L;
    }

    /**
     * Check if the punishment is active.
     *
     * @return True if active
     */
    public boolean isActive() {
        return isPermanent() || expiry > System.currentTimeMillis();
    }

    /**
     * Get the end date of the punishment.
     * "Permanent" or "Expired" will be returned depending on
     * the punishment's state.
     *
     * @return End date of the punishment
     */
    public String getEndDate() {
        if (!isActive()) return "Expired";
        if (isPermanent()) return "Permanent";

        return FORMAT.format(new Date(expiry));
    }

    /**
     * Save the punishment to the storage type.
     *
     * @see PunishmentStorage#save(Punishment)
     */
    public void save() {
        storage.save(this);
    }

    /**
     * Delete the punishment from the storage type.
     *
     * @see PunishmentStorage#delete(Punishment)
     */
    public void delete() {
        storage.delete(this);
    }

}
