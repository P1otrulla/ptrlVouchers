package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.config.ConfigService;
import dev.piotrulla.vouchers.notification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "voucher")
@Permission("voucher.admin")
public class VoucherCommand {

    private final NoticeService noticeService;
    private final VoucherService voucherService;
    private final VoucherItemService voucherItemService;
    private final ConfigService configService;

    public VoucherCommand(
            NoticeService noticeService,
            VoucherService voucherService,
            VoucherItemService voucherItemService,
            ConfigService configService
    ) {
        this.noticeService = noticeService;
        this.voucherService = voucherService;
        this.voucherItemService = voucherItemService;
        this.configService = configService;
    }

    @Execute(name = "give")
    void give(@Context CommandSender commandSender, @Arg Player player, @Arg Voucher voucher, @Arg int amount) {
        if (amount < 1 || amount > 64) {
            this.noticeService.create().notice(notice -> notice.invalidAmount).viewer(commandSender).send();
            return;
        }

        this.voucherItemService.giveVoucher(player, voucher, amount);

        this.noticeService.create()
                .notice(notice -> notice.voucherGiven)
                .placeholder("{PLAYER}", player.getName())
                .placeholder("{VOUCHER}", voucher.name())
                .viewer(player)
                .send();

        this.noticeService.create()
                .notice(notice -> notice.voucherReceived)
                .placeholder("{PLAYER}", player.getName())
                .placeholder("{VOUCHER}", voucher.name())
                .viewer(commandSender)
                .send();
    }

    @Execute(name = "list")
    void list(@Context CommandSender commandSender) {
        this.noticeService.create()
                .notice(notice -> notice.vouchersList)
                .placeholder("{VOUCHERS}", String.join(", ", this.voucherService.findAllVouchers()))
                .viewer(commandSender)
                .send();
    }

    @Execute(name = "reload")
    void reload(@Context CommandSender commandSender) {
        this.configService.reload();
        this.noticeService.create()
                .notice(notice -> notice.configReloaded)
                .viewer(commandSender)
                .send();
    }
}