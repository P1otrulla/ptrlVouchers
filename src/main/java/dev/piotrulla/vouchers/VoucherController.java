package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.notification.NoticeService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VoucherController implements Listener {

    private final VoucherService voucherService;
    private final VoucherItemService voucherItemService;
    private final NoticeService noticeService;

    public VoucherController(
            VoucherService voucherService,
            VoucherItemService voucherItemService,
            NoticeService noticeService
    ) {
        this.voucherService = voucherService;
        this.voucherItemService = voucherItemService;
        this.noticeService = noticeService;
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack == null) {
            return;
        }

        this.voucherService.findVoucher(itemStack).ifPresent(voucher -> {
            event.setCancelled(true);

            Player player = event.getPlayer();

            this.noticeService.create()
                    .notice(notice -> notice.voucherUsed)
                    .placeholder("{VOUCHER}", voucher.name())
                    .player(player.getUniqueId())
                    .send();

            this.voucherItemService.giveVoucherRewards(player, voucher);
        });
    }
}