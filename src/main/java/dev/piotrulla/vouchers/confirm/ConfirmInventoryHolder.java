package dev.piotrulla.vouchers.confirm;

import dev.piotrulla.vouchers.MiniMessageHolder;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ConfirmInventoryHolder implements InventoryHolder, MiniMessageHolder {

    private final Inventory inventory;

    public ConfirmInventoryHolder(Server server, int size, String title) {
        this.inventory = server.createInventory(this, size, MINI_MESSAGE.deserialize(title));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
