package me.mrfishcakes.propunish.storage.types.json;

import de.leonhard.storage.Json;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.storage.PunishmentStorage;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.PunishmentCreator;
import me.mrfishcakes.propunish.types.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class JsonPunishmentStorage extends PunishmentStorage {

    private File dataFolder;

    public JsonPunishmentStorage(@NotNull ProPunishPlugin plugin) {
        super(plugin);
        this.dataFolder = new File(plugin.getPluginFolder(), "punishments");
        if (dataFolder.mkdirs()) plugin.log(Level.INFO, "Punishments folder created!");
    }

    @Override
    public void save(@NotNull Punishment punishment) {
        CompletableFuture.runAsync(() -> {
            final Json data = new Json(new File(dataFolder, punishment.getTarget() +
                    ".json"));

            String prefix = punishment.getType().name() + "." + punishment.getId();

            data.getFileData().insert(prefix + ".targetName", punishment.getTargetName());
            data.getFileData().insert(prefix + ".issuer", punishment.getIssuer().toString());
            data.getFileData().insert(prefix + ".issuerName", punishment.getIssuerName());
            data.getFileData().insert(prefix + ".reason", punishment.getReason());
            data.set(prefix + ".expiry", punishment.getExpiry());
        });
    }

    @Override
    public void delete(@NotNull Punishment punishment) {
        CompletableFuture.runAsync(() -> {
            final File dataFile = new File(dataFolder, punishment.getTarget() +
                    ".json");
            if (!dataFile.exists()) return;

            final Json data = new Json(dataFile);

            String prefix = punishment.getType().name() + "." + punishment.getId();

            data.getFileData().remove(prefix + ".targetName");
            data.getFileData().remove(prefix + ".issuer");
            data.getFileData().remove(prefix + ".issuerName");
            data.getFileData().remove(prefix + ".expiry");
            data.getFileData().remove(prefix + ".reason");
            data.remove(prefix);
        });
    }

    @Override
    public List<Punishment> getAllPunishments(@NotNull UUID target) {
        final List<Punishment> found = new LinkedList<>();
        final File dataFile = new File(dataFolder, target + ".json");

        if (!dataFile.exists()) {
            return found;
        }

        found.addAll(getByType(target, PunishmentType.BAN));
        found.addAll(getByType(target, PunishmentType.MUTE));

        found.sort(plugin.getPunishmentComparator());

        return found;
    }

    @Override
    public List<Punishment> getPunishmentsByType(@NotNull UUID target, @NotNull PunishmentType type) {
        return getByType(target, type);
    }

    @Override
    public void disable() {
        dataFolder = null;
    }

    private List<Punishment> getByType(UUID target, PunishmentType type) {
        List<Punishment> found = new LinkedList<>();
        Json data = new Json(new File(dataFolder, target + ".json"));

        data.getSection(type.name()).singleLayerKeySet().forEach(key -> {
            String prefix = type.name() + "." + key;

            final int id = Integer.parseInt(key);
            final String targetName = data.getString(prefix + ".targetName");
            final UUID issuer = UUID.fromString(data.getString(prefix + ".issuer"));
            final String issuerName = data.getString(prefix + ".issuerName");
            final long expiry = data.getLong(prefix + ".expiry");
            final String reason = data.getString(prefix + ".reason");

            found.add(PunishmentCreator.create(id, target, issuer)
                    .targetName(targetName).issuerName(issuerName).duration(expiry)
                    .reason(reason).type(PunishmentType.MUTE).get(this));
        });

        return found;
    }
}
