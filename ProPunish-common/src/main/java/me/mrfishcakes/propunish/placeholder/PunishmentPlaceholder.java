package me.mrfishcakes.propunish.placeholder;

import me.mrfishcakes.propunish.types.Punishment;

public abstract class PunishmentPlaceholder {

    /**
     * Method to easily create placeholder modified strings
     *
     * @see Placeholder
     */
    protected String replacePlaceholders(String input, Punishment ban) {
        return Placeholder.create(input, ban).transform();
    }

}