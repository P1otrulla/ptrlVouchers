package dev.piotrulla.vouchers.util.legacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Legacy {

    public static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component component(String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }

    private Legacy() {
    }
}
