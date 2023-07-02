package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import dev.piotrulla.vouchers.util.ItemUtil;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import panda.utilities.text.Formatter;

@Route(name = "voucher")
@Permission("voucher.admin")
public class VoucherCommand {

    private final NotificationBroadcaster broadcaster;
    private final VoucherRepository repository;
    private final VoucherConfig config;

    public VoucherCommand(NotificationBroadcaster broadcaster, VoucherRepository repository, VoucherConfig config) {
        this.broadcaster = broadcaster;
        this.repository = repository;
        this.config = config;
    }

    @Execute(route = "give", required = 3)
    void give(CommandSender commandSender, @Arg Player player, @Arg Voucher voucher, @Arg int amount) {
        if (amount < 1) {
            this.broadcaster.sendAnnounce(commandSender, this.config.invalidAmount);
            return;
        }

        Formatter formatter = new Formatter()
                .register("{PLAYER}", player.getName())
                .register("{VOUCHER}", voucher.name());

        ItemStack voucherItem = voucher.item().clone();
        voucherItem.setAmount(amount);

        ItemUtil.giveItem(player, voucherItem);

        this.broadcaster.sendAnnounce(commandSender, this.config.voucherGiven, formatter);
        this.broadcaster.sendAnnounce(player, this.config.voucherRecived, formatter);
    }

    @Execute(route = "list")
    void list(CommandSender commandSender) {
        Formatter formatter = new Formatter()
                .register("{VOUCHERS}", String.join(", ", this.repository.findAllVouchers()));

        this.broadcaster.sendAnnounce(commandSender, this.config.vouchersList, formatter);
    }
}
