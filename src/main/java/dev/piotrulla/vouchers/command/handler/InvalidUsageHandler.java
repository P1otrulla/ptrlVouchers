package dev.piotrulla.vouchers.command.handler;

import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

public class InvalidUsageHandler implements Handler<CommandSender, Schematic> {

    private final NotificationBroadcaster broadcaster;
    private final VoucherConfig config;

    public InvalidUsageHandler(NotificationBroadcaster broadcaster, VoucherConfig config) {
        this.broadcaster = broadcaster;
        this.config = config;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        if (schematic.isOnlyFirst()) {
            this.broadcaster.sendAnnounce(sender, this.config.correctUsage, new Formatter().register("{USAGE}", schematic.first()));

            return;
        }

        this.broadcaster.sendAnnounce(sender, this.config.correctUsageHeader);

        schematic.getSchematics().forEach(usage -> this.broadcaster.sendAnnounce(sender, this.config.correctUsageEntry, new Formatter().register("{USAGE}", usage)));
    }
}
