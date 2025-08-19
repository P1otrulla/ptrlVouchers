package dev.piotrulla.vouchers.litecommands;

import dev.piotrulla.vouchers.notification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class MissingPermissionHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    private final NoticeService noticeService;

    public MissingPermissionHandlerImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        MissingPermissions missingPermissions,
        ResultHandlerChain<CommandSender> chain
    ) {

        String joinedText = missingPermissions.asJoinedText();

        this.noticeService.create()
            .notice(notice -> notice.noPermission)
            .placeholder("{PERMISSION}", joinedText)
            .viewer(invocation.sender())
            .send();
    }
}
