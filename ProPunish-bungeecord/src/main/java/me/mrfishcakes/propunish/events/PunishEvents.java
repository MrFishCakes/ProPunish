package me.mrfishcakes.propunish.events;

import com.google.common.base.Preconditions;
import me.mrfishcakes.propunish.placeholder.PunishmentPlaceholder;
import me.mrfishcakes.propunish.plugin.ProPunishPlugin;
import me.mrfishcakes.propunish.types.Punishment;
import me.mrfishcakes.propunish.types.collections.BanCollection;
import me.mrfishcakes.propunish.types.collections.MuteCollection;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import static net.md_5.bungee.event.EventPriority.HIGHEST;

public final class PunishEvents extends PunishmentPlaceholder implements Listener {

    private final ProPunishPlugin plugin;

    public PunishEvents(@NotNull final ProPunishPlugin plugin) {
        Preconditions.checkNotNull(plugin, "ProPunishPlugin cannot be null");

        this.plugin = plugin;
    }

    @EventHandler(priority = HIGHEST)
    private void onLogin(final LoginEvent event) {
        final UUID id = event.getConnection().getUniqueId();
        List<Punishment> bans = new BanCollection(id, plugin.getStorage()).active(true).get();

        if (bans.isEmpty()) return;

        final Punishment ban = bans.get(0);
        if (ban == null) {
            event.setCancelled(true);
            event.setCancelReason(fromText(ChatColor.RED + "An internal error occurred when " +
                    "logging in!"));
            throw new NullPointerException("Ban was found but returned null");
        }

        String screen = ban.isPermanent() ? plugin.getConfigYaml().getString(
                "Messages.PermanentBan") : plugin.getConfigYaml().getString(
                "Messages.TemporaryBan");

        event.setCancelled(true);
        event.setCancelReason(fromText(replacePlaceholders(screen, ban)));
    }

    @EventHandler(priority = HIGHEST)
    private void onChat(final ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        CompletableFuture<Boolean> supplier = CompletableFuture.supplyAsync(() -> {
            List<Punishment> mutes = new MuteCollection(player.getUniqueId(), plugin.getStorage())
                    .active(true).get();
            if (mutes.isEmpty()) return false;

            final Punishment mute = mutes.get(0);

            String screen = mute.isPermanent() ? plugin.getConfigYaml().getString(
                    "Messages.PermanentMute") : plugin.getConfigYaml().getString(
                    "Messages.TemporaryMute");

            player.sendMessage(fromText(replacePlaceholders(screen, mute)));
            return true;
        });

        try {
            event.setCancelled(supplier.get());
        } catch (InterruptedException | ExecutionException ex) {
            plugin.log(Level.SEVERE, "There was an error fetching mute", ex);
            event.setCancelled(true);
        }
    }

    private BaseComponent[] fromText(String input) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                input.replaceAll("''", "'")));
    }

}
