package dev.piotrulla.vouchers.bridge.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class VaultMoneyService implements MoneyService {

    private final Economy economy;

    public VaultMoneyService(Economy economy) {
        this.economy = economy;
    }

    @Override
    public void deposit(Player player, double money) {
        this.economy.depositPlayer(player, money);
    }

    @Override
    public void withdraw(Player player, double money) {
        this.economy.withdrawPlayer(player, money);
    }
}
