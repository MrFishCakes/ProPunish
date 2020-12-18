package me.mrfishcakes.propunish.event;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.mrfishcakes.propunish.types.Punishment;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class SpigotPunishEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Punishment punishment;

    public SpigotPunishEvent(@NotNull final Punishment punishment) {
        Preconditions.checkNotNull(punishment, "Punishment cannot be null");

        this.punishment = punishment;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlersList() {
        return HANDLERS;
    }
}
