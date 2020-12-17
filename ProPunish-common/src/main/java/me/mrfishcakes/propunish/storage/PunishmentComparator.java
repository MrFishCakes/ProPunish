package me.mrfishcakes.propunish.storage;

import me.mrfishcakes.propunish.types.Punishment;

import java.util.Comparator;

public final class PunishmentComparator implements Comparator<Punishment> {

    @Override
    public int compare(Punishment first, Punishment second) {
        return Integer.compare(first.getId(), second.getId());
    }
}
