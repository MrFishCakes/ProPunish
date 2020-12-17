package me.mrfishcakes.propunish.events;

import com.google.common.base.Preconditions;
import me.mrfishcakes.propunish.placeholder.PunishmentPlaceholder;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.collections.BanCollection;
import me.mrfishcakes.propunish.types.collections.MuteCollection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static org.bukkit.event.EventPriority.HIGHEST;

public final class PunishEvents extends PunishmentPlaceholder implements Listener {

    private final ProPunishPlugin plugin;

    public PunishEvents(@NotNull final ProPunishPlugin plugin) {
        Preconditions.checkNotNull(plugin, "ProPunishPlugin cannot be null");

        this.plugin = plugin;
    }

    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    private void onAsyncLogin(final AsyncPlayerPreLoginEvent event) {
        final UUID id = event.getUniqueId();
        List<Punishment> bans = new BanCollection(id, plugin.getStorage()).active(true).get();

        if (bans.isEmpty()) return;

        final Punishment ban = bans.get(0);
        if (ban == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    ChatColor.RED + "An internal error occurred when logging in!");
            throw new NullPointerException("Ban was found but returned null");
        }

        String screen = ban.isPermanent() ? plugin.getConfigYaml().getString(
                "Messages.PermanentBan") : plugin.getConfigYaml().getString(
                "Messages.TemporaryBan");

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.
                translateAlternateColorCodes('&', replacePlaceholders(screen, ban)));
    }

    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    private void onAsyncChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID id = player.getUniqueId();
        List<Punishment> mutes = new MuteCollection(id, plugin.getStorage()).active(true).get();
        if (mutes.isEmpty()) return;

        final Punishment mute = mutes.get(0);

        String screen = mute.isPermanent() ? plugin.getConfigYaml().getString(
                "Messages.PermanentMute") : plugin.getConfigYaml().getString(
                "Messages.TemporaryMute");

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                replacePlaceholders(screen, mute)));
        event.setCancelled(true);
    }

}
