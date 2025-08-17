package dev.piotrulla.vouchers.notification;

import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.bukkit.BukkitMultification;
import com.eternalcode.multification.translation.TranslationProvider;
import dev.piotrulla.vouchers.VoucherConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class NoticeService extends BukkitMultification<VoucherConfig> {

    private final VoucherConfig voucherConfig;
    private final MiniMessage miniMessage;

    public NoticeService(VoucherConfig voucherConfig, MiniMessage miniMessage) {
        this.voucherConfig = voucherConfig;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull TranslationProvider<VoucherConfig> translationProvider() {
        return locale -> this.voucherConfig;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> commandSender;
    }
}
