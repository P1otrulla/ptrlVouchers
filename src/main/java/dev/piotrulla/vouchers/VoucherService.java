package dev.piotrulla.vouchers;

import dev.piotrulla.vouchers.config.ConfigService;
import dev.piotrulla.vouchers.bridge.vault.MoneyService;
import dev.piotrulla.vouchers.util.ItemUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VoucherService {

    private final VoucherDataConfig dataConfig;
    private final ConfigService configService;
    private final MoneyService moneyService;

    public VoucherService(VoucherDataConfig dataConfig, ConfigService configService, MoneyService moneyService) {
        this.dataConfig = dataConfig;
        this.configService = configService;
        this.moneyService = moneyService;
    }

    public void giveVoucher(Player player, Voucher voucher) {
        this.removeItemInHand(player);

        this.dataConfig.addUsedVoucher();
        this.configService.save(this.dataConfig);

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

    void removeItemInHand(Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand().clone();

        if (hand.getAmount() == 1) {
            player.getInventory().setItemInMainHand(null);

            return;
        }

        hand.setAmount(hand.getAmount() - 1);
        player.getInventory().setItemInMainHand(hand);
    }
}
