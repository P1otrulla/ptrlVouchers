package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.Voucher;
import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.VoucherItemService;
import dev.piotrulla.vouchers.notification.NoticeService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmInventoryController implements Listener {

    private final VoucherItemService voucherItemService;
    private final NoticeService noticeService;
    private final VoucherConfig config;

    public ConfirmInventoryController(
            VoucherItemService voucherItemService,
            NoticeService noticeService,
            VoucherConfig config
    ) {
        this.voucherItemService = voucherItemService;
        this.noticeService = noticeService;
        this.config = config;
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ConfirmInventoryHolder holder)) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (item.isSimilar(this.config.confirm.yesItem.toItemStack())) {
            player.closeInventory();

            ItemStack originalItem = holder.getItem();
            Voucher voucher = holder.getVoucher();

            if (!player.getInventory().containsAtLeast(originalItem, 1)) {
                return;
            }

            this.voucherItemService.giveVoucherRewards(player, voucher);

            this.noticeService.create()
                    .notice(notice -> notice.voucherUsed)
                    .placeholder("{VOUCHER}", voucher.name())
                    .player(player.getUniqueId())
                    .send();

            return;
        }

        if (item.isSimilar(this.config.confirm.noItem.toItemStack())) {
            player.closeInventory();
        }
    }
}
