package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.VoucherConfig;
import dev.piotrulla.vouchers.VoucherItemService;
import dev.piotrulla.vouchers.VoucherService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmInventoryController implements Listener {

    private final VoucherItemService voucherItemService;
    private final VoucherService voucherService;
    private final VoucherConfig config;

    public ConfirmInventoryController(
            VoucherItemService voucherItemService,
            VoucherService voucherService,
            VoucherConfig config
    ) {
        this.voucherItemService = voucherItemService;
        this.voucherService = voucherService;
        this.config = config;
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ConfirmInventoryHolder)) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        if (item.isSimilar(this.config.confirm.yesItem)) {
            event.getWhoClicked().closeInventory();


            return;
        }

        if (item.isSimilar(this.config.confirm.noItem)) {
            event.getWhoClicked().closeInventory();
            return;
        }
    }
}
