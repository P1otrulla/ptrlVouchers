package dev.piotrulla.vouchers.litecommands;

import dev.piotrulla.vouchers.notification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    private final NoticeService noticeService;

    public InvalidUsageHandlerImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        InvalidUsage<CommandSender> result,
        ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            this.noticeService.create()
                .notice(notice -> notice.correctUsage)
                .placeholder("{USAGE}", schematic.first())
                .viewer(invocation.sender())
                .send();

            return;
        }

        this.noticeService.create()
            .notice(notice -> notice.correctUsageHeader)
            .placeholder("{USAGE}", schematic.first())
            .viewer(invocation.sender())
            .send();

        for (String usage : schematic.all()) {
            this.noticeService.create()
                .notice(notice -> notice.correctUsageEntry)
                .placeholder("{USAGE}", usage)
                .viewer(invocation.sender())
                .send();
        }
    }
}
