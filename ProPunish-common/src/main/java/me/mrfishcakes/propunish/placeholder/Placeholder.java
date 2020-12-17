package me.mrfishcakes.propunish.placeholder;

import me.mrfishcakes.propunish.types.Punishment;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Placeholder {

    /**
     * Create a new {@link Placeholder} instance.
     *
     * @param input      String to replace
     * @param punishment Punishment to call data from
     * @return A new Placeholder instance
     */
    public static Placeholder create(@NotNull final String input,
                                     @NotNull final Punishment punishment) {
        return new Placeholder(input, punishment);
    }

    private String original;
    private final Map<String, String> replacements;

    private Placeholder(@NotNull final String original, @NotNull Punishment punishment) {
        this.original = original;
        this.replacements = new LinkedHashMap<>();

        add("\\{punish_issuer_name\\}", punishment.getIssuerName());
        add("\\{punish_issuer_uuid\\}", punishment.getIssuer().toString());
        add("\\{punish_target_name\\}", punishment.getTargetName());
        add("\\{punish_target_uuid\\}", punishment.getTarget().toString());
        add("\\{punish_reason\\}", punishment.getReason());
        add("\\{punish_expire_date\\}", punishment.getEndDate());
        add("\\{punish_id\\}", String.valueOf(punishment.getId()));
    }

    /**
     * Add a key and value to the map for replacement.
     *
     * @param placeholder Text to be replaced
     * @param replacement Text to replace with
     * @return Modified {@link Placeholder}
     */
    public Placeholder add(@NotNull final String placeholder, @NotNull final String replacement) {
        replacements.put(placeholder, replacement);
        return this;
    }

    /**
     * Transform the original string by replacing the placeholders.
     *
     * @return Modified string
     */
    public final String transform() {
        replacements.forEach((placeholder, replacement) ->
                original = original.replaceAll(placeholder, replacement));

        return original;
    }

}
