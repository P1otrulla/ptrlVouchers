package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.VoucherConfig;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfirmInventory {

    private final VoucherConfig config;
    private final Server server;

    public ConfirmInventory(VoucherConfig config, Server server) {
        this.config = config;
        this.server = server;
    }

    public void open(Player player, ItemStack voucherItem) {
        ConfirmInventoryHolder holder = new ConfirmInventoryHolder(this.server, this.config.confirm.size, this.config.confirm.title);
        Inventory inventory = holder.getInventory();

        if (this.config.confirm.cloneItem) {
            inventory.setItem(this.config.confirm.cloneItemSlot, voucherItem.clone());
        }

        for (int slot : this.config.confirm.yesSlots) {
            inventory.setItem(slot, this.config.confirm.yesItem);
        }

        for (int slot : this.config.confirm.noSlots) {
            inventory.setItem(slot, this.config.confirm.noItem);
        }

        player.openInventory(inventory);
    }
}
