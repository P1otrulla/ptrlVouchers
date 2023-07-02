package dev.piotrulla.vouchers.notification;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.time.Duration;

public class NotificationBroadcaster {

    private static final Formatter EMPTY_FORMATTER = new Formatter();

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;
    private Title.Times titleTimes;

    public NotificationBroadcaster(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void sendAnnounce(Object sender, Notification notification) {
        this.sendAnnounce(sender, notification, EMPTY_FORMATTER);
    }

    public void sendAnnounce(Object sender, Notification notification, Formatter formatter) {
        Audience audience = this.toAudience(sender);

        String message = formatter.format(notification.message());

        for (NotificationType notificationType : notification.types()) {
            switch (notificationType) {
                case TITLE -> audience.showTitle(Title.title(this.miniMessage.deserialize(message), Component.text(""), this.times()));
                case SUBTITLE -> audience.showTitle(Title.title(Component.text(""), this.miniMessage.deserialize(message), this.times()));
                case ACTIONBAR -> audience.sendActionBar(this.miniMessage.deserialize(message));
                case CHAT -> audience.sendMessage(this.miniMessage.deserialize(message));
                case NONE -> {}
            }
        }
    }

    private Audience toAudience(Object sender) {
        if (sender instanceof Player player) {
            return this.audienceProvider.player(player.getUniqueId());
        }

        return this.audienceProvider.console();
    }

    private Title.Times times() {
        if (this.titleTimes == null) {
            this.titleTimes = Title.Times.times(
                    Duration.ofSeconds(1),
                    Duration.ofSeconds(2),
                    Duration.ofSeconds(1)
            );
        }

        return this.titleTimes;
    }
}
