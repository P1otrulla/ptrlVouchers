package dev.piotrulla.vouchers.command.handler;

import dev.piotrulla.vouchers.notification.Notification;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.command.CommandSender;

public class NotificationHandler implements Handler<CommandSender, Notification> {

    private final NotificationBroadcaster broadcaster;

    public NotificationHandler(NotificationBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Notification value) {
        this.broadcaster.sendAnnounce(sender, value);
    }
}
