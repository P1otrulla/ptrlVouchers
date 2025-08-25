package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.MiniMessageHolder;
import dev.piotrulla.vouchers.Voucher;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConfirmInventoryHolder implements InventoryHolder, MiniMessageHolder {

    private final Inventory inventory;
    private final Voucher voucher;
    private final ItemStack item;

    public ConfirmInventoryHolder(Server server, int size, String title, Voucher voucher, ItemStack item) {
        this.inventory = server.createInventory(this, size, MINI_MESSAGE.deserialize(title));
        this.voucher = voucher;
        this.item = item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Voucher getVoucher() {
        return this.voucher;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
