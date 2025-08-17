package dev.piotrulla.vouchers;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface MiniMessageHolder {

    MiniMessage MINI_MESSAGE = MiniMessage.builder()
        .postProcessor(new AdventureLegacyColorPostProcessor())
        .preProcessor(new AdventureLegacyColorPreProcessor())
        .build();
}
