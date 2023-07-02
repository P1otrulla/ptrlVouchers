package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.notification.NotificationBroadcaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import panda.utilities.text.Formatter;

public class VoucherController implements Listener {

    private final NotificationBroadcaster broadcaster;
    private final VoucherRepository repository;
    private final VoucherService service;
    private final VoucherConfig config;

    public VoucherController(NotificationBroadcaster broadcaster, VoucherRepository repository, VoucherService service, VoucherConfig config) {
        this.broadcaster = broadcaster;
        this.repository = repository;
        this.service = service;
        this.config = config;
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack == null) {
            return;
        }

        this.repository.findVoucher(itemStack).ifPresent(voucher -> {
            Formatter formatter = new Formatter()
                    .register("{VOUCHER}", voucher.name());

            Player player = event.getPlayer();

            this.broadcaster.sendAnnounce(player, this.config.voucherUsed, formatter);

            this.service.giveVoucher(player, voucher);
        });
    }
}
