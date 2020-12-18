package me.mrfishcakes.propunish.event;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.mrfishcakes.propunish.types.Punishment;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public final class BungeePunishEvent extends Event {

    @Getter
    private final Punishment punishment;

    public BungeePunishEvent(@NotNull final Punishment punishment) {
        Preconditions.checkNotNull(punishment, "Punishment cannot be null");

        this.punishment = punishment;
    }

}
