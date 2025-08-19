package dev.piotrulla.vouchers;

import com.eternalcode.commons.bukkit.ItemUtil;
import dev.piotrulla.vouchers.bridge.vault.MoneyService;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class VoucherItemService {

    private final MoneyService moneyService;
    private final NamespacedKey voucherKey;

    public VoucherItemService(MoneyService moneyService, Plugin plugin) {
        this.moneyService = moneyService;
        this.voucherKey = new NamespacedKey(plugin, "voucher_id");
    }

    public ItemStack createVoucherItem(Voucher voucher, int amount) {
        ItemStack voucherItem = voucher.item().clone();
        voucherItem.setAmount(amount);

        ItemMeta meta = voucherItem.getItemMeta();
        if (meta != null) {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(voucherKey, PersistentDataType.STRING, voucher.name());
            voucherItem.setItemMeta(meta);
        }

        return voucherItem;
    }

    public void giveVoucher(Player player, Voucher voucher, int amount) {
        if (amount < 1 || amount > 64) {
            return;
        }

        ItemStack voucherItem = createVoucherItem(voucher, amount);
        ItemUtil.giveItem(player, voucherItem);
    }

    public boolean isValidVoucher(ItemStack itemStack, Voucher voucher) {
        if (itemStack == null || voucher == null) {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        String voucherName = dataContainer.get(voucherKey, PersistentDataType.STRING);

        return voucher.name().equals(voucherName);
    }

    public String getVoucherNameFromItem(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        return dataContainer.get(voucherKey, PersistentDataType.STRING);
    }

    public void giveVoucherRewards(Player player, Voucher voucher) {
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand == null || hand.getAmount() < 1) {
            return;
        }

        if (!isValidVoucher(hand, voucher)) {
            return;
        }

        if (hand.getAmount() == 1) {
            player.getInventory().setItemInMainHand(null);
        }
        else {
            hand.setAmount(hand.getAmount() - 1);
        }

        voucher.commands().forEach(command -> {
            String formattedCommand = command.replace("{PLAYER}", player.getName());
            Server server = player.getServer();
            server.dispatchCommand(server.getConsoleSender(), formattedCommand);
        });

        voucher.items().forEach(item -> ItemUtil.giveItem(player, item));

        if (this.moneyService != null && voucher.money() > 0) {
            this.moneyService.deposit(player, voucher.money());
        }
    }
}