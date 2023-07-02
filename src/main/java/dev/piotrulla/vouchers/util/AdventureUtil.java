package dev.piotrulla.vouchers.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public final class AdventureUtil {

    private AdventureUtil() {

    }

    private final static Component RESET_ITALIC = Component.text()
            .decoration(TextDecoration.ITALIC, false)
            .build();

    public static Component reset(Component component) {
        return RESET_ITALIC.append(component);
    }
}
