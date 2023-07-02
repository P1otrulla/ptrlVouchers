package dev.piotrulla.vouchers.command.handler;

import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;
import panda.utilities.text.Joiner;

public class PermissionHandler implements Handler<CommandSender, RequiredPermissions> {

    private final NotificationBroadcaster broadcaster;
    private final VoucherConfig config;

    public PermissionHandler(NotificationBroadcaster broadcaster, VoucherConfig config) {
        this.broadcaster = broadcaster;
        this.config = config;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, RequiredPermissions permissions) {
        Formatter formatter = new Formatter()
                .register("{PERMISSIONS}", Joiner.on(", ")
                        .join(permissions.getPermissions())
                        .toString());

        this.broadcaster.sendAnnounce(commandSender, this.config.noPermission, formatter);
    }
}

